package ru.practicum.ewm.event.service.admin.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.dto.EventAdminReqDtoIn;
import ru.practicum.ewm.event.dto.EventFullDtoOut;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.admin.IEventAdminService;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventAdminService implements IEventAdminService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public EventFullDtoOut update(EventAdminReqDtoIn eventAdminReqDtoIn,
                                  Long eventId) {
        Event existingEvent = getEventOrThrow(eventId);
        Event eventToUpdate = updateEventInternal(existingEvent, eventAdminReqDtoIn);
        System.out.println(eventToUpdate);
        return EventMapper.toEventFullDtoOut(eventRepository.save(eventToUpdate), 0L, 0L);
    }

    private Event getEventOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Event with id %d not found", eventId)));
    }

    private Event updateEventInternal(Event existingEvent,
                                      EventAdminReqDtoIn updatedEvent) {
        EventDateAdminUpdater.updateEventDateTimeInternal(existingEvent, updatedEvent);
        EventStateAdminUpdater.updateEventStateInternal(existingEvent, updatedEvent);

        Category newCategory = updatedEvent.getCategory() != null ? getCategoryOrThrow(
                updatedEvent.getCategory()) : existingEvent.getCategory();
        EventFieldsAdminUpdater.updateEventFieldsInternal(existingEvent, updatedEvent, newCategory);
        return existingEvent;
    }

    private Category getCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Category with id %d not found", categoryId)));
    }
}
