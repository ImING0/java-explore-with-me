package ru.practicum.ewm.event.service.authorized.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.error.DataConflictException;
import ru.practicum.ewm.event.dto.event.EventUserUpdDtoIn;
import ru.practicum.ewm.event.dto.event.StateUserAction;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.Duration;
import java.time.LocalDateTime;

@UtilityClass
class EventDateAuthorizedUpdater {
    /**
     * Проверяет, что интервал между датой публикации и датой начала события больше или равен hours
     *
     * @param publishedOn дата публикации
     * @param eventStart  дата начала события
     * @param hours       количество часов
     * @return true, если интервал между датой публикации и датой начала события больше или равен hours
     */
    private boolean isIntervalMoreThanHours(LocalDateTime publishedOn,
                                            LocalDateTime eventStart,
                                            Long hours) {
        Duration duration = Duration.between(publishedOn, eventStart);
        return duration.toHours() >= hours;
    }

    public void updateEventDateTimeInternal(Event existingEvent,
                                            EventUserUpdDtoIn updatedEvent) {
        StateUserAction newState = updatedEvent.getStateAction();
        EventState currentState = existingEvent.getState();
        LocalDateTime existingEventDateTime = existingEvent.getEventDate();
        LocalDateTime updatedEventDateTime = updatedEvent.getEventDate();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Long hoursLimit = 2L;

        /*если даты нет и нет нового статуса, значит это простое изменение каких-то других полй
        мы не заходим в условия
        Здесь проверяем только если есть новый статус и дата*/
        if ((updatedEventDateTime != null) && (newState != null)) {
            switch (currentState) {
                case CANCELED:
                    switch (newState) {
                        case CANCEL_REVIEW:
                        case SEND_TO_REVIEW:
                            if (!isIntervalMoreThanHours(currentDateTime, updatedEventDateTime,
                                    hoursLimit)) {
                                throw new DataConflictException(String.format(
                                        "Event date and time must be at least %d hours from now, but it's %s",
                                        hoursLimit, updatedEventDateTime));
                            } else {
                                existingEvent.setEventDate(updatedEventDateTime);
                                break;
                            }
                    }
                case PENDING:
                    switch (newState) {
                        case SEND_TO_REVIEW:
                        case CANCEL_REVIEW:
                            if (!isIntervalMoreThanHours(currentDateTime, updatedEventDateTime,
                                    hoursLimit)) {
                                throw new DataConflictException(String.format(
                                        "Event date and time must be at least %d hours from now, but it's %s",
                                        hoursLimit, updatedEventDateTime));
                            } else {
                                existingEvent.setEventDate(updatedEventDateTime);
                                break;
                            }
                    }
                case PUBLISHED:
                    throw new DataConflictException(String.format(
                            "Event date and time can't be changed after publication, but it's %s",
                            existingEventDateTime));
            }
        }

        /*Пришло только новое время, без состояния*/
        if (updatedEventDateTime != null && newState == null) {
            switch (currentState) {
                case CANCELED:
                case PENDING:
                    if (!isIntervalMoreThanHours(currentDateTime, updatedEventDateTime,
                            hoursLimit)) {
                        throw new DataConflictException(String.format(
                                "Event date and time must be at least %d hours from now, but it's %s",
                                hoursLimit, updatedEventDateTime));
                    } else {
                        existingEvent.setEventDate(updatedEventDateTime);
                        break;
                    }
                case PUBLISHED:
                    throw new DataConflictException(String.format(
                            "Event date and time can't be changed after publication, but it's %s",
                            existingEventDateTime));
            }
        }
        /*Пришло только состояние, без времени*/
        if (newState != null && updatedEventDateTime == null) {
            switch (currentState) {
                case CANCELED:
                    switch (newState) {
                        case CANCEL_REVIEW:
                            break;
                        case SEND_TO_REVIEW:
                            if (!isIntervalMoreThanHours(currentDateTime, existingEventDateTime,
                                    hoursLimit)) {
                                throw new DataConflictException(String.format(
                                        "Event date and time must be at least %d hours from now, but it's %s",
                                        hoursLimit, existingEventDateTime));
                            } else {
                                break;
                            }
                    }
                case PENDING:
                    switch (newState) {
                        case SEND_TO_REVIEW:
                            if (!isIntervalMoreThanHours(currentDateTime, existingEventDateTime,
                                    hoursLimit)) {
                                throw new DataConflictException(String.format(
                                        "Event date and time must be at least %d hours from now, but it's %s",
                                        hoursLimit, existingEventDateTime));
                            } else {
                                break;
                            }
                        case CANCEL_REVIEW:
                            break;
                    }
            }
        }
    }
}
