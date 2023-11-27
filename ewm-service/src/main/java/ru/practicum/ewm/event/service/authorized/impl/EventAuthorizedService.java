package ru.practicum.ewm.event.service.authorized.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.error.DataConflictException;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.dto.EventFullDtoOut;
import ru.practicum.ewm.event.dto.NewEventDtoIn;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.authorized.IEventAuthorizedService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventAuthorizedService implements IEventAuthorizedService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public EventFullDtoOut create(NewEventDtoIn newEventDtoIn,
                                  Long userId) {
        User initiator = getUserOrThrow(userId);
        Category category = getCategoryOrThrow(newEventDtoIn.getCategory());
        checkEventBeforeCreate(newEventDtoIn);
        Long confirmedRequests = 0L;
        Event eventToSave = EventMapper.toEvent(newEventDtoIn, initiator, category);
        System.out.println(eventToSave);
        return EventMapper.toEventFullDtoOut(eventRepository.save(eventToSave), confirmedRequests);
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

    private void checkEventBeforeCreate(NewEventDtoIn newEventDtoIn) {
        /*дата и время на которые намечено событие не может быть раньше,
         чем через два часа от текущего момента*/

        /*Сначала думал задать минус пару секунд из расчета задержек соединения
         * но потом решил, что на фронте то будут числа кратные минутам и тп,
         * так то решил опустить этот момент.*/
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime eventDateTime = newEventDtoIn.getEventDate();
        Duration duration = Duration.between(currentDateTime, eventDateTime);
        if (duration.toHours() < 2) {
            throw new DataConflictException(String.format(
                    "Event date and time must be at least 2 hours from now, but it's %s",
                    eventDateTime.toString()));
        }
    }
}
