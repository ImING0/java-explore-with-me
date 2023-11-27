package ru.practicum.ewm.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.RequestDtoOut;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.user.model.User;

/**
 * Маппер запросов на участие в мероприятии
 */
@UtilityClass
public class RequestMapper {
    /**
     * Создание запроса на участие в мероприятии
     *
     * @param requester пользователь
     * @param event     мероприятие
     * @return запрос на участие в мероприятии
     */
    public Request toRequest(User requester,
                             Event event) {
        return Request.builder()
                .requester(requester)
                .event(event)
                .status(RequestStatus.PENDING)
                .build();
    }

    /**
     * Создание DTO запроса на участие в мероприятии
     *
     * @param request запрос на участие в мероприятии
     * @return DTO запроса на участие в мероприятии
     */
    public RequestDtoOut toRequestDtoOut(Request request) {
        return RequestDtoOut.builder()
                .id(request.getId())
                .requester(request.getRequester()
                        .getId())
                .event(request.getEvent()
                        .getId())
                .status(request.getStatus())
                .created(request.getCreatedOn())
                .build();
    }
}
