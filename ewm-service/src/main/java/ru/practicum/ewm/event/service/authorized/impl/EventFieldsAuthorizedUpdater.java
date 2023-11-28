package ru.practicum.ewm.event.service.authorized.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.event.EventUserUpdDtoIn;
import ru.practicum.ewm.event.model.Event;

@UtilityClass
public class EventFieldsAuthorizedUpdater {

    public void updateEventFieldsInternal(Event existingEvent,
                                          EventUserUpdDtoIn updatedEvent,
                                          Category newCategory) {
        existingEvent.setTitle(updatedEvent.getTitle() != null ? updatedEvent.getTitle()
                : existingEvent.getTitle());
        existingEvent.setAnnotation(
                updatedEvent.getAnnotation() != null ? updatedEvent.getAnnotation()
                        : existingEvent.getAnnotation());
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
