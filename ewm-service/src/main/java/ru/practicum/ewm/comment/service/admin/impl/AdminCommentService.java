package ru.practicum.ewm.comment.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.comment.service.admin.IAdminCommentService;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class AdminCommentService implements IAdminCommentService {
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    private Event getEventOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Event with id %d not found", eventId)));
    }
}
