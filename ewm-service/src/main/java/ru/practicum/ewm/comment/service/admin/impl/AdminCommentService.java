package ru.practicum.ewm.comment.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.dto.CommentDtoIn;
import ru.practicum.ewm.comment.dto.CommentDtoOut;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentState;
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

    @Override
    public CommentDtoOut update(Long eventId,
                                Long commId,
                                CommentDtoIn commentDtoIn) {
        Event event = getEventOrThrow(eventId);
        Comment comment = getCommentOrThrow(commId);
        comment.setText(commentDtoIn.getText());
        return CommentMapper.toCommentDtoOut(commentRepository.save(comment));
    }

    @Override
    public CommentDtoOut delete(Long eventId,
                                Long commId) {
        Event event = getEventOrThrow(eventId);
        Comment comment = getCommentOrThrow(commId);
        comment.setState(CommentState.DELETED_BY_ADMIN);
        comment.setPinned(false);
        return CommentMapper.toCommentDtoOut(commentRepository.save(comment));
    }

    private Event getEventOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Event with id %d not found", eventId)));
    }

    public Comment getCommentOrThrow(Long commId) {
        return commentRepository.findById(commId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Comment with id %d not found", commId)));
    }
}
