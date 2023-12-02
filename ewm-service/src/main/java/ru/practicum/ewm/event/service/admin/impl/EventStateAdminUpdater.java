package ru.practicum.ewm.event.service.admin.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.error.DataConflictException;
import ru.practicum.ewm.event.dto.event.EventAdminUpdDtoIn;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

/**
 * Обновление состояния мероприятия администратором
 */
@UtilityClass
class EventStateAdminUpdater {
    /**
     * Обновление состояния мероприятия администратором
     *
     * @param existingEvent существующее мероприятие
     * @param updatedEvent  обновленное мероприятие
     */
    public void updateEventStateInternal(Event existingEvent,
                                         EventAdminUpdDtoIn updatedEvent) {
        if (updatedEvent.getStateAction() == null) {
            return;
        }
        if (updatedEvent.getStateAction() != null) {
            switch (existingEvent.getState()) {
                case PENDING:
                    switch (updatedEvent.getStateAction()) {
                        case PUBLISH_EVENT:
                            existingEvent.setState(EventState.PUBLISHED);
                            break;
                        case REJECT_EVENT:
                            existingEvent.setState(EventState.CANCELED);
                            break;
                        default:
                            throw new DataConflictException(String.format(
                                    "Invalid state action %s for event with id %d in state %s",
                                    updatedEvent.getStateAction(), existingEvent.getId(),
                                    existingEvent.getState()));
                    }
                    break;
                default:
                    throw new DataConflictException(String.format(
                            "Event with id %d can't be processed for action %s, because its state is %s",
                            existingEvent.getId(), updatedEvent.getStateAction(),
                            existingEvent.getState()));
            }
        }
    }
}
