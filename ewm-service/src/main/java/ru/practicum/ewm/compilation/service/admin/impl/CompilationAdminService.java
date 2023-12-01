package ru.practicum.ewm.compilation.service.admin.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDtoOut;
import ru.practicum.ewm.compilation.dto.NewCompilationDtoIn;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDtoIn;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.service.admin.ICompilationAdminService;
import ru.practicum.ewm.error.DataConflictException;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminService implements ICompilationAdminService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDtoOut create(NewCompilationDtoIn newCompilationDtoIn) {
        /*Для начала создадим пустую подборку*/
        Compilation compilation = compilationRepository.saveAndFlush(
                CompilationMapper.toCompilation(newCompilationDtoIn));
        log.info("Created compilation with id {}", compilation.getId());
        Set<Long> eventIds = newCompilationDtoIn.getEvents();
        /*Если нет евентов значит пустая подборка. Уже создали, посто вернем ее.*/
        if (eventIds == null || eventIds.isEmpty()) {
            return CompilationMapper.toCompilationDtoOut(compilation);
        }
        /*Проверяем, что все переданные id событий существуют*/
        List<Event> existingEvents = getEventsFromIdsOrThrow404IfSomeEventNotFound(eventIds);

        existingEvents.forEach(event1 -> event1.setCompilation(compilation));
        eventRepository.saveAllAndFlush(existingEvents);
        compilation.setEvents(existingEvents);
        /*Пытался запросить в рамках этой транзакции сохраненную компиляцию со всеми эвентами,
         * но увы, в маппер почему - то при всех раскладах попадала компиляция с евентами null,
         * хотя все остальные поля были заполнены. Ну зато минус лишний запрос к бд)*/
        return CompilationMapper.toCompilationDtoOut(compilation);
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        Compilation compilation = getCompilationOrThrow(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public CompilationDtoOut update(UpdateCompilationDtoIn updateCompilationDtoIn,
                                    Long compId) {
        Compilation existingCompilation = getCompilationOrThrow(compId);
        if (updateCompilationDtoIn.getTitle() != null && !updateCompilationDtoIn.getTitle()
                .isBlank()) {
            if (existingCompilation.getTitle()
                    .equals(updateCompilationDtoIn.getTitle())) {
                throw new DataConflictException(
                        String.format("Compilation with id %s already has title %s", compId,
                                updateCompilationDtoIn.getTitle()));
            } else {
                existingCompilation.setTitle(updateCompilationDtoIn.getTitle());
            }
        }
        if (updateCompilationDtoIn.getPinned() != null) {
            existingCompilation.setPinned(updateCompilationDtoIn.getPinned());
        }
        compilationRepository.saveAndFlush(existingCompilation);
        if (updateCompilationDtoIn.getEvents() != null) {
            if (updateCompilationDtoIn.getEvents()
                    .isEmpty()) {
                List<Event> eventToDeleteFromComp = existingCompilation.getEvents();
                if (!eventToDeleteFromComp.isEmpty()) {
                    eventToDeleteFromComp.forEach(event -> event.setCompilation(null));
                    eventRepository.saveAllAndFlush(eventToDeleteFromComp);
                }
            } else {
                Set<Long> eventIds = updateCompilationDtoIn.getEvents();
                List<Event> existingEvents = getEventsFromIdsOrThrow404IfSomeEventNotFound(
                        eventIds);
                existingEvents.forEach(event -> event.setCompilation(existingCompilation));
                eventRepository.saveAllAndFlush(existingEvents);
                existingCompilation.setEvents(existingEvents);
            }
        }
        return CompilationMapper.toCompilationDtoOut(existingCompilation);
    }

    private Compilation getCompilationOrThrow(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Compilation with id %s not found", id)));
    }

    private List<Event> getEventsFromIdsOrThrow404IfSomeEventNotFound(Set<Long> eventIds) {
        QEvent event = QEvent.event;
        BooleanExpression predict = event.isNotNull()
                .and(event.id.in(eventIds));
        List<Event> existingEvents = (List<Event>) eventRepository.findAll(predict);
        if (existingEvents.size() != eventIds.size()) {
            Set<Long> existingEventIds = existingEvents.stream()
                    .map(Event::getId)
                    .collect(Collectors.toSet());
            eventIds.removeAll(existingEventIds);
            throw new ResourceNotFoundException(
                    String.format("Events with ids %s not found", eventIds));
        }
        return existingEvents;
    }
}
