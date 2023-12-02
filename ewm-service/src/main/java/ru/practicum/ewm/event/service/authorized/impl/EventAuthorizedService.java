package ru.practicum.ewm.event.service.authorized.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.error.DataConflictException;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.dto.event.EventFullDtoOut;
import ru.practicum.ewm.event.dto.event.EventShortDtoOut;
import ru.practicum.ewm.event.dto.event.EventUserUpdDtoIn;
import ru.practicum.ewm.event.dto.event.NewEventDtoIn;
import ru.practicum.ewm.event.dto.request.EventRequestStatusUpdDtoIn;
import ru.practicum.ewm.event.dto.request.EventRequestStatusUpdDtoOut;
import ru.practicum.ewm.event.dto.request.RequestStatusUserUpdDtoIn;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.authorized.IEventAuthorizedService;
import ru.practicum.ewm.request.dto.RequestDtoOut;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventAuthorizedService implements IEventAuthorizedService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public EventFullDtoOut create(NewEventDtoIn newEventDtoIn,
                                  Long userId) {
        User initiator = getUserOrThrow(userId);
        Category category = getCategoryOrThrow(newEventDtoIn.getCategory());
        checkEventBeforeCreate(newEventDtoIn);
        Event eventToSave = EventMapper.toEvent(newEventDtoIn, initiator, category);
        return EventMapper.toEventFullDtoOut(eventRepository.save(eventToSave));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDtoOut> getAllForCurrentUser(Long userId,
                                                       Pageable pageable) {
        User initiator = getUserOrThrow(userId);
        return eventRepository.findAllByInitiator(initiator, pageable)
                .stream()
                .map(EventMapper::toEventShortDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDtoOut getByIdForCurrentUser(Long userId,
                                                 Long eventId) {
        User initiator = getUserOrThrow(userId);
        return EventMapper.toEventFullDtoOut(
                eventRepository.findByIdAndInitiator(eventId, initiator)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                String.format("Event with id %d not found", eventId))));
    }

    @Override
    @Transactional
    public EventFullDtoOut updateForCurrentUser(EventUserUpdDtoIn updatedEvent,
                                                Long userId,
                                                Long eventId) {
        Event updatedEventToSave = updateEventInternal(userId, eventId, updatedEvent);
        return EventMapper.toEventFullDtoOut(eventRepository.save(updatedEventToSave));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDtoOut> getAllRequestByEventIdForCurrentUser(Long userId,
                                                                    Long eventId) {
        User initiator = getUserOrThrow(userId);
        Event event = getEventOrThrow(eventId);
        return requestRepository.findAllByEventAndEventInitiator(event, initiator)
                .stream()
                .map(RequestMapper::toRequestDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdDtoOut updateRequestsStatusForCurrentUser(EventRequestStatusUpdDtoIn eventRequestStatusUpdDtoIn,
                                                                          Long userId,
                                                                          Long eventId) {
        User initiator = getUserOrThrow(userId);
        Event event = getEventOrThrow(eventId);
        Long participantLimit = Long.valueOf(event.getParticipantLimit());
        Long currentConfirmedRequests = event.getConfirmedRequests();

        Set<Long> requestsIdsForUpdate = eventRequestStatusUpdDtoIn.getRequestIds();
        List<Request> requests = requestRepository.findAllByIdIn(requestsIdsForUpdate);
        Set<Long> existingRequestsIds = requests.stream()
                .map(Request::getId)
                .collect(Collectors.toSet());
        Set<Long> notFoundIds = new HashSet<>(requestsIdsForUpdate);
        notFoundIds.removeAll(existingRequestsIds);
        /*Если отправитель заявки не владелец, отправим not found*/
        if (!event.getInitiator()
                .getId()
                .equals(initiator.getId())) throw new ResourceNotFoundException(
                String.format("Event with id %d not found for user with id %d", eventId, userId));

        /*Если пришел список реквестов, которых нет в бд, то прервем операцию и вернем список id,
         которых нет в БД*/
        if (!notFoundIds.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Requests with ids %s not found", notFoundIds));
        }


        /*нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
         (Ожидается код ошибки 409)*/
        if (currentConfirmedRequests.equals(participantLimit)) {
            throw new DataConflictException(
                    String.format("Event with id %d has reached the limit of confirmed requests",
                            eventId));
        }

        /*статус можно изменить только у заявок, находящихся в состоянии ожидания
         (Ожидается код ошибки 409)*/
        List<RequestDtoOut> badRequests = requests.stream()
                .filter(request -> !request.getStatus()
                        .equals(RequestStatus.PENDING))
                .map(RequestMapper::toRequestDtoOut)
                .collect(Collectors.toList());
        if (!badRequests.isEmpty()) {
            throw new DataConflictException(String.format(
                    "Requests with ids %s can't be updated, because they are not in pending state",
                    badRequests));
        }

        /*если при подтверждении данной заявки, лимит заявок для события исчерпан,
         то все неподтверждённые заявки необходимо отклонить
         ТЗ настолько неоднозначно, ну ок...*/
        long remainingLimit = participantLimit - currentConfirmedRequests;
        RequestStatus requestStatusToUpdate = eventRequestStatusUpdDtoIn.getStatus()
                .equals(RequestStatusUserUpdDtoIn.CONFIRMED) ? RequestStatus.CONFIRMED
                : RequestStatus.REJECTED;

        for (Request request : requests) {
            if (remainingLimit > 0) {
                request.setStatus(requestStatusToUpdate);
                remainingLimit--;
            } else {
                request.setStatus(RequestStatus.REJECTED);
            }
        }
        return RequestMapper.toEventRequestStatusUpdDtoOut(requestRepository.saveAll(requests));
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

    private Category getCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Category with id %d not found", categoryId)));
    }

    /**
     * Проверяет, что дата и время на которые намечено событие не может быть раньше чем через два часа от текущего момента
     *
     * @param newEventDtoIn данные для создания события
     */
    private void checkEventBeforeCreate(NewEventDtoIn newEventDtoIn) {
        /*дата и время на которые намечено событие не может быть раньше,
         чем через два часа от текущего момента*/
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime eventDateTime = newEventDtoIn.getEventDate();
        Duration duration = Duration.between(currentDateTime, eventDateTime);
        if (duration.toHours() < 2) {
            throw new DataConflictException(String.format(
                    "Event date and time must be at least 2 hours from now, but it's %s",
                    eventDateTime.toString()));
        }
    }

    /**
     * Обновляет событие в соответствии с данными из updatedEvent
     *
     * @param userId       идентификатор пользователя
     * @param eventId      идентификатор события
     * @param updatedEvent данные для обновления события
     * @return обновленное событие
     */
    private Event updateEventInternal(Long userId,
                                      Long eventId,
                                      EventUserUpdDtoIn updatedEvent) {
        Event existingEvent = getEventOrThrow(eventId);
        User initiator = getUserOrThrow(userId);
        /*Если пользователь не владелец, то выбросим что событие не найдено*/
        if (!existingEvent.getInitiator()
                .getId()
                .equals(initiator.getId())) throw new ResourceNotFoundException(
                String.format("Event with id %d not found for user with id %d", eventId, userId));
        /*изменить можно только отмененные события или события в состоянии ожидания модерации
         (Ожидается код ошибки 409)*/
        if (existingEvent.getState()
                .equals(EventState.PUBLISHED)) throw new DataConflictException(
                String.format("Event with id %d is published and can't be updated", eventId));

        EventDateAuthorizedUpdater.updateEventDateTimeInternal(existingEvent, updatedEvent);

        EventStateAuthorizedUpdater.updateEventStateInternal(existingEvent, updatedEvent);
        Category newCategory = updatedEvent.getCategory() != null ? getCategoryOrThrow(
                updatedEvent.getCategory()) : existingEvent.getCategory();
        EventFieldsAuthorizedUpdater.updateEventFieldsInternal(existingEvent, updatedEvent,
                newCategory);
        return existingEvent;
    }
}
