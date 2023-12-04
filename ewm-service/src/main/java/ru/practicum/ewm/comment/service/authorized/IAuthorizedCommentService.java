package ru.practicum.ewm.comment.service.authorized;

import ru.practicum.ewm.comment.dto.CommentDtoIn;
import ru.practicum.ewm.comment.dto.CommentDtoOut;

/**
 * Сервис для работы с комментариями, доступный авторизованным пользователям.
 */
public interface IAuthorizedCommentService {
    /**
     * Создание комментария.
     *
     * @param userId  id пользователя
     * @param eventId id события
     * @return созданный комментарий
     */
    CommentDtoOut create(Long userId,
                         Long eventId,
                         CommentDtoIn commentDtoIn);
}
