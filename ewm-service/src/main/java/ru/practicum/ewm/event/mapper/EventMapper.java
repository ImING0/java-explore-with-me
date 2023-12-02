package ru.practicum.ewm.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.event.EventFullDtoOut;
import ru.practicum.ewm.event.dto.event.EventShortDtoOut;
import ru.practicum.ewm.event.dto.event.NewEventDtoIn;
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
                .confirmedRequests(0L) //при создании события количество одобренных заявок равно 0
                .state(EventState.PENDING)
                .eventDate(newEventDtoIn.getEventDate())
                .location(Location.builder()
                        .lat(newEventDtoIn.getLocation()
                                .getLat())
                        .lon(newEventDtoIn.getLocation()
                                .getLon())
                        .build())
                .paid(newEventDtoIn.isPaid())
                .requestModeration(newEventDtoIn.isRequestModeration())
                .views(0L) //при создании события количество просмотров равно 0
                .build();
    }

    /**
     * Метод для преобразования сущности Event в EventFullDtoOut
     *
     * @param event событие
     * @return EventFullDtoOut
     */
    public EventFullDtoOut toEventFullDtoOut(Event event) {
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
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .location(LocationMapper.toDtoOut(event.getLocation()))
                .paid(event.getPaid())
                .requestModeration(event.getRequestModeration())
                .build();
    }

    public EventShortDtoOut toEventShortDtoOut(Event event) {
        return EventShortDtoOut.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDtoOut(event.getCategory()))
                .initiator(UserMapper.toUserShortDtoOut(event.getInitiator()))
                .eventDate(event.getEventDate())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .paid(event.getPaid())
                .build();
    }
}
