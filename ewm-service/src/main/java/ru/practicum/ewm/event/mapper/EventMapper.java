package ru.practicum.ewm.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.EventFullDtoOut;
import ru.practicum.ewm.event.dto.NewEventDtoIn;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

/**
 * Класс для преобразования сущностей Event в DTO и обратно
 */
@UtilityClass
public class EventMapper {
    /**
     * Метод для преобразования NewEventDtoIn в Event для сохранения в БД
     *
     * @param newEventDtoIn DTO события
     * @param user          пользователь, создающий событие
     * @param category      категория события
     * @return Event
     */
    public Event toEvent(NewEventDtoIn newEventDtoIn,
                         User user,
                         Category category) {
        return Event.builder()
                .title(newEventDtoIn.getTitle())
                .annotation(newEventDtoIn.getAnnotation())
                .description(newEventDtoIn.getDescription())
                .initiator(user)
                .category(category)
                .participantLimit(newEventDtoIn.getParticipantLimit())
                .state(EventState.PENDING)
                .eventDate(newEventDtoIn.getEventDate())
                .location(Location.builder()
                        .lat(newEventDtoIn.getLocation()
                                .getLat())
                        .lon(newEventDtoIn.getLocation()
                                .getLon())
                        .build())
                .paid(newEventDtoIn.getPaid())
                .requestModeration(newEventDtoIn.getRequestModeration())
                .views(0L)
                .build();
    }

    /**
     * Метод для преобразования сущности Event в EventFullDtoOut
     *
     * @param event             событие
     * @param confirmedRequests количество одобренных заявок на участие в данном событии
     * @return EventFullDtoOut
     */
    public EventFullDtoOut toEventFullDtoOut(Event event,
                                             Long confirmedRequests) {
        return EventFullDtoOut.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .initiator(UserMapper.toUserShortDtoOut(event.getInitiator()))
                .category(CategoryMapper.toDtoOut(event.getCategory()))
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .views(event.getViews())
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .location(LocationMapper.toDtoOut(event.getLocation()))
                .paid(event.getPaid())
                .requestModeration(event.getRequestModeration())
                .build();
    }
}
