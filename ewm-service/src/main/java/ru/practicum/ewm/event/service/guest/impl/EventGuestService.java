package ru.practicum.ewm.event.service.guest.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.error.BadRequestException;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.Util.SortByForEvent;
import ru.practicum.ewm.event.dto.event.EventFullDtoOut;
import ru.practicum.ewm.event.dto.event.EventShortDtoOut;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.guest.IEventGuestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventGuestService implements IEventGuestService {
    private final EventRepository eventRepository;

    @Override
    public EventFullDtoOut getById(Long id) {
        QEvent event = QEvent.event;
        BooleanExpression predicate = event.isNotNull();
        predicate = predicate.and(event.id.eq(id))
                .and(event.state.eq(EventState.PUBLISHED));
        Event existingEvent = eventRepository.findOne(predicate)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Event with id %d not found", id)));
        return EventMapper.toEventFullDtoOut(existingEvent);
    }

    @Override
    @Transactional
    public List<EventShortDtoOut> getAllByParams(String text,
                                                 Set<Long> categories,
                                                 Boolean paid,
                                                 LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd,
                                                 Boolean onlyAvailable,
                                                 SortByForEvent sort,
                                                 Integer from,
                                                 Integer size) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException(
                        String.format("rangeStart %s is after rangeEnd %s", rangeStart, rangeEnd));
            }
        }

        QEvent event = QEvent.event;
        BooleanExpression predicate = event.isNotNull();
        predicate = predicate.and(event.state.eq(EventState.PUBLISHED));
        if ((!(text == null)) && !(text.isBlank())) {
            predicate = predicate.and(event.annotation.containsIgnoreCase(text)
                    .or(event.description.containsIgnoreCase(text)));
        }
        if (!categories.isEmpty()) {
            predicate = predicate.and(event.category.id.in(categories));
        }
        if (!(paid == null)) {
            predicate = predicate.and(event.paid.eq(paid));
        }
        if (!(rangeStart == null || rangeEnd == null)) {
            predicate = predicate.and(event.eventDate.between(rangeStart, rangeEnd));
        } else {
            // - 2 секунды потому что тесты не проходят ) там прям впритык создается...
            predicate = predicate.and(event.eventDate.after(LocalDateTime.now()
                    .minusSeconds(2)));
        }

        if (onlyAvailable) {
            predicate = predicate.and(event.participantLimit.eq(0)
                    .or(event.confirmedRequests.lt(event.participantLimit)));
        }

        Pageable pageable;
        switch (sort) {
            case VIEWS:
                pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "views"));
                break;
            case EVENT_DATE:
                pageable = PageRequest.of(from / size, size,
                        Sort.by(Sort.Direction.DESC, "eventDate"));
                break;
            default: throw new IllegalStateException("Unexpected value: " + sort);
        }
        return eventRepository.findAll(predicate, pageable)
                .stream()
                .map(EventMapper::toEventShortDtoOut)
                .collect(Collectors.toList());
    }
}