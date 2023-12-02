package ru.practicum.ewm.event.service.admin.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.event.EventAdminUpdDtoIn;
import ru.practicum.ewm.event.model.Event;

/**
 * Класс для обновления полей мероприятия администратором
 */
@UtilityClass
class EventFieldsAdminUpdater {
    /**
     * Обновляет поля мероприятия
     *
     * @param existingEvent существующее мероприятие
     * @param updatedEvent  новые данные мероприятия
     * @param newCategory   новая категория
     */
    public void updateEventFieldsInternal(Event existingEvent,
                                          EventAdminUpdDtoIn updatedEvent,
                                          Category newCategory) {
        existingEvent.setTitle(updatedEvent.getTitle() != null ? updatedEvent.getTitle()
                : existingEvent.getTitle());
        existingEvent.setAnnotation(
                updatedEvent.getAnnotation() != null ? updatedEvent.getAnnotation()
                        : existingEvent.getAnnotation());
        existingEvent.setDescription(
                updatedEvent.getDescription() != null ? updatedEvent.getDescription()
                        : existingEvent.getDescription());
        existingEvent.setCategory(newCategory);
        existingEvent.setEventDate(updatedEvent.getEventDate() != null ? updatedEvent.getEventDate()
                : existingEvent.getEventDate());
        if (updatedEvent.getLocation() != null) {
            existingEvent.getLocation()
                    .setLat(updatedEvent.getLocation()
                            .getLat());
            existingEvent.getLocation()
                    .setLon(updatedEvent.getLocation()
                            .getLon());
        }
        existingEvent.setPaid(
                updatedEvent.getPaid() != null ? updatedEvent.getPaid() : existingEvent.getPaid());
        existingEvent.setRequestModeration(
                updatedEvent.getRequestModeration() != null ? updatedEvent.getRequestModeration()
                        : existingEvent.getRequestModeration());
        existingEvent.setParticipantLimit(
                updatedEvent.getParticipantLimit() != null ? updatedEvent.getParticipantLimit()
                        : existingEvent.getParticipantLimit());
    }
}
