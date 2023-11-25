package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.dto.EventFullDtoOut;
import ru.practicum.ewm.event.dto.NewEventDtoIn;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.IEventService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public EventFullDtoOut create(NewEventDtoIn newEventDtoIn,
                                  Long userId) {
        User initiator = getUserOrThrow(userId);
        Category category = getCategoryOrThrow(newEventDtoIn.getCategory());
        /* Хотел сначала сделать тут запросы к серверу статистике через клиент, но потом понял,
        что это не имеет смысла, так как при создании и так всё по нулям.*/
        Long views = 0L;
        Long confirmedRequests = 0L;
        Event eventToSave = EventMapper.toEvent(newEventDtoIn, initiator, category);
        System.out.println(eventToSave);
        return EventMapper.toEventFullDtoOut(eventRepository.save(eventToSave), views,
                confirmedRequests);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with id %d not found", userId)));
    }

    private Category getCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Category with id %d not found", categoryId)));
    }
}
