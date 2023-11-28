package ru.practicum.ewm.request.service.authorized.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.error.DataConflictException;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.dto.RequestDtoOut;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.request.service.authorized.IRequestAuthorizedService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestAuthorizedService implements IRequestAuthorizedService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public RequestDtoOut create(Long userId,
                                Long eventId) {
        User requester = getUserOrThrow(userId);
        Event event = getEventOrThrow(eventId);
        Request requestToSave = RequestMapper.toRequest(requester, event);
        checkRequestBeforeCreate(requestToSave);
        return RequestMapper.toRequestDtoOut(requestRepository.save(requestToSave));
    }

    @Override
    public List<RequestDtoOut> getAllForCurrentUser(Long userId) {
        return requestRepository.findAllByRequester(getUserOrThrow(userId))
                .stream()
                .map(RequestMapper::toRequestDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDtoOut cancel(Long requesterId,
                                Long requestId) {
        User user = getUserOrThrow(requesterId);
        Request existingRequest = getRequestOrThrow(requestId);
        checkRequestBeforeCancel(existingRequest, user);
        existingRequest.setStatus(RequestStatus.REJECTED);
        return RequestMapper.toRequestDtoOut(requestRepository.save(existingRequest));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with id %d not found", userId)));
    }

    private Event getEventOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Event with id %d not found", eventId)));
    }

    private Request getRequestOrThrow(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Request with id %d not found", requestId)));
    }

    /**
     * Проверка запроса на участие в мероприятии перед отменой
     *
     * @param request   запрос
     * @param requester пользователь, который хочет отменить запрос
     */
    private void checkRequestBeforeCancel(Request request,
                                          User requester) {
        /*Проверим, что пользователь владелец запроса*/
        if (!request.getRequester()
                .getId()
                .equals(requester.getId())) {
            throw new DataConflictException(String.format(
                    "User with id %d can't cancel request with id %d because he is not owner of this request",
                    requester.getId(), request.getId()));
        }
        /*Проверяем что событие еще не закончилось*/
        if (request.getEvent()
                .getEventDate()
                .isBefore(LocalDateTime.now())) {
            throw new DataConflictException(String.format(
                    "User with id %d can't cancel request with id %d because event with id %d is already finished",
                    requester.getId(), request.getId(), request.getEvent()
                            .getId()));
        }
    }

    /**
     * Проверка запроса на участие в мероприятии перед созданием
     *
     * @param request запрос
     */
    private void checkRequestBeforeCreate(Request request) {
        /*инициатор события не может добавить запрос на участие в своём событии
         (Ожидается код ошибки 409)*/
        if (request.getEvent()
                .getInitiator()
                .getId()
                .equals(request.getRequester()
                        .getId())) {
            throw new DataConflictException(String.format(
                    "User with id %d can't create request for event with id %d because he is initiator of this event",
                    request.getRequester()
                            .getId(), request.getEvent()
                            .getId()));
        }
        /*нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)*/
        if (!request.getEvent()
                .getState()
                .equals(EventState.PUBLISHED)) {
            throw new DataConflictException(String.format(
                    "User with id %d can't create request for event with id %d because this event"
                            + " is not published, it's state is %s", request.getRequester()
                            .getId(), request.getEvent()
                            .getId(), request.getEvent()
                            .getState()));
        }
        /*нельзя добавить повторный запрос (Ожидается код ошибки 409)*/
        if (requestRepository.existsByRequesterAndEvent(request.getRequester(),
                request.getEvent())) {
            throw new DataConflictException(String.format(
                    "User with id %d can't create request for event with id %d because he already has request for this event",
                    request.getRequester()
                            .getId(), request.getEvent()
                            .getId()));
        }
        /*если у события достигнут лимит запросов на участие и лимит вообще установлен -
        необходимо вернуть ошибку (Ожидается код ошибки 409)*/
        if ((request.getEvent()
                .getConfirmedRequests() >= request.getEvent()
                .getParticipantLimit()) && (request.getEvent()
                .getParticipantLimit() != 0L)) {
            throw new DataConflictException(String.format(
                    "User with id %d can't create request for event with id %d because this event"
                            + " has reached limit of requests", request.getRequester()
                            .getId(), request.getEvent()
                            .getId()));
        }

        /*если для события отключена пре-модерация запросов на участие,
         то запрос должен автоматически перейти в состояние подтвержденного*/
        if (!request.getEvent()
                .getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
    }
}
