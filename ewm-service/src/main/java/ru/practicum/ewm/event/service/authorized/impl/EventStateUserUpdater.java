package ru.practicum.ewm.event.service.authorized.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.event.dto.event.EventUserUpdDtoIn;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

/**
 * Обновление состояния события для авторизованного пользователя.
 */
@UtilityClass
class EventStateUserUpdater {
    /**
     * Обновление состояния события для авторизованного пользователя.
     *
     * @param existingEvent Существующее событие
     * @param updatedEvent  Обновленное событие
     */
    public void updateEventStateInternal(Event existingEvent,
                                         EventUserUpdDtoIn updatedEvent) {
        /*Если есть новое состояние - то обновляем.*/
        if (!(updatedEvent.getStateAction() == null)) {
            switch (existingEvent.getState()) {
                case CANCELED:
                    switch (updatedEvent.getStateAction()) {
                        case CANCEL_REVIEW:
                            break; // действие не требуется
                        case SEND_TO_REVIEW:
                            existingEvent.setState(EventState.PENDING);
                            break;
                    }
                    break;
                case PENDING:
                    switch (updatedEvent.getStateAction()) {
                        case CANCEL_REVIEW:
                            existingEvent.setState(EventState.CANCELED);
                            break;
                        case SEND_TO_REVIEW:
                            // действие не требуется.
                            break;
                    }
                    break;
            }
        }
    }
}
