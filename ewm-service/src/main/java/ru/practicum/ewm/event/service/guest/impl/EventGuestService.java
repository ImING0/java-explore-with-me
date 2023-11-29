package ru.practicum.ewm.event.service.guest.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.Util.SortByForEvent;
import ru.practicum.ewm.event.dto.event.EventShortDtoOut;
import ru.practicum.ewm.event.mapper.EventMapper;
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
        Sort eventSortBy = null;
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
            predicate = predicate.and(event.eventDate.after(LocalDateTime.now()));
        }

        if (onlyAvailable) {
            predicate = predicate.and(event.participantLimit.eq(0)
                    .or(event.confirmedRequests.lt(event.participantLimit)));
        }
        if (sort != null) {
            switch (sort) {
                case VIEWS:
                    eventSortBy = Sort.by(Sort.Direction.DESC, "views");
                    break;
                case EVENT_DATE:
                    eventSortBy = Sort.by(Sort.Direction.DESC, "eventDate");
                    break;
            }
        } else {
            eventSortBy = Sort.by(Sort.Direction.DESC, "eventDate");
        }
        Pageable pageable = PageRequest.of(from / size, size, eventSortBy);
        return eventRepository.findAll(predicate, pageable)
                .stream()
                .map(EventMapper::toEventShortDtoOut)
                .collect(Collectors.toList());
    }
}
