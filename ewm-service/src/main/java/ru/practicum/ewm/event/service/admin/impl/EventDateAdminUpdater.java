package ru.practicum.ewm.event.service.admin.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.error.DataConflictException;
import ru.practicum.ewm.event.dto.event.EventAdminUpdDtoIn;
import ru.practicum.ewm.event.dto.event.StateAdminAction;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Класс для обновления даты и времени события администратором
 */
@UtilityClass
class EventDateAdminUpdater {

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

    /**
     * Обновляет дату и время события в зависимости от текущего состояния и нового состояния
     *
     * @param existingEvent существующее событие
     * @param updatedEvent  событие с новыми данными
     */
    public void updateEventDateTimeInternal(Event existingEvent,
                                            EventAdminUpdDtoIn updatedEvent) {
        StateAdminAction newState = updatedEvent.getStateAction();
        EventState currentState = existingEvent.getState();
        LocalDateTime existingEventDateTime = existingEvent.getEventDate();
        LocalDateTime updatedEventDateTime = updatedEvent.getEventDate();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Long hoursLimit = 1L;

        /*если даты нет и нет нового статуса, значит это простое изменение каких-то других полй
        мы не заходим в условия
        Здесь проверяем только если есть новый статус и дата*/
        if ((updatedEventDateTime != null) && (newState != null)) {
            boolean isValidInterval = isIntervalMoreThanHours(LocalDateTime.now(),
                    updatedEventDateTime, hoursLimit); // Новая дата за час до публикации?
            switch (currentState) {
                case PENDING:
                    switch (newState) {
                        case PUBLISH_EVENT:
                            if (!isValidInterval) {
                                throw new DataConflictException(String.format(
                                        "Event with id %d can't be published, because it's date "
                                                + "is %s and it's less than %d hours from now",
                                        existingEvent.getId(), existingEventDateTime, hoursLimit));
                            } else {
                                existingEvent.setEventDate(updatedEventDateTime);
                            }
                            break;
                        case REJECT_EVENT:
                            /*В целом тут нам уже не важно, какая будет дата, ставим что дали*/
                            existingEvent.setEventDate(updatedEventDateTime);
                            break;
                    }
                    break;
                case PUBLISHED:
                    /*Если событие опубликовано, то ставим запрет на какое - либо изменение даты и
                    времени*/
                    throw new DataConflictException(String.format(
                            "Event with id %d can't be updated, because it's state is %s",
                            existingEvent.getId(), existingEvent.getState()));
                case CANCELED:
                        /*Если событие отменено, то ставим запрет на какое - либо изменение даты и
                        времени*/
                    throw new DataConflictException(String.format(
                            "Event with id %d can't be updated, because it's state is %s",
                            existingEvent.getId(), existingEvent.getState()));
            }
            return;
        }

        /*Пришло только новое время, без состояния*/
        if (updatedEventDateTime != null && newState == null) {
            boolean isValidInterval = isIntervalMoreThanHours(LocalDateTime.now(),
                    updatedEventDateTime, hoursLimit); // Новая дата за час до публикации?
            switch (currentState) {
                case PENDING:
                case PUBLISHED:
                    /*В целом обновляем, если от текущего момента до события больше часа*/
                    if (!isValidInterval) {
                        throw new DataConflictException(String.format(
                                "Event with id %d can't be updated, because it's date "
                                        + "is %s and it's less than %d hours from now",
                                existingEvent.getId(), existingEventDateTime, hoursLimit));
                    } else {
                        existingEvent.setEventDate(updatedEventDateTime);
                        return;
                    }
                case CANCELED:
                    throw new DataConflictException(String.format(
                            "Event with id %d can't be updated, because it's state is %s",
                            existingEvent.getId(), existingEvent.getState()));
            }
            return;
        }
        /*Пришло только состояние, без времени*/
        if (newState != null && updatedEventDateTime == null) {
            switch (currentState) {
                case PENDING:
                    switch (newState) {
                        case PUBLISH_EVENT:
                            if (!isIntervalMoreThanHours(LocalDateTime.now(), existingEventDateTime,
                                    hoursLimit)) {
                                throw new DataConflictException(String.format(
                                        "Event with id %d can't be published, because it's date "
                                                + "is %s and it's less than %d hours from now",
                                        existingEvent.getId(), existingEventDateTime, hoursLimit));
                            } else {
                                /*Ставим дату публикации события*/
                                existingEvent.setEventDate(currentDateTime);
                                return;
                            }
                        case REJECT_EVENT:
                            /*Оставляем без изменений*/
                            break;
                    }
                    break;
                case PUBLISHED:
                    switch (newState) {
                        case PUBLISH_EVENT:
                        case REJECT_EVENT:
                            /*Оставляем без изменений*/
                            break;
                    }
                case CANCELED:
                    switch (newState) {
                        case PUBLISH_EVENT:
                        case REJECT_EVENT:
                            /*Оставляем без изменений*/
                            break;
                    }
            }
            return;
        }
    }
}
