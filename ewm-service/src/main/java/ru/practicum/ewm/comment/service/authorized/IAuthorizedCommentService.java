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

    /**
     * Обновление комментария.
     *
     * @param userId       id пользователя
     * @param eventId      id события
     * @param commId       id комментария
     * @param commentDtoIn новый текст комментария
     * @return обновленный комментарий
     */
    CommentDtoOut update(Long userId,
                         Long eventId,
                         Long commId,
                         CommentDtoIn commentDtoIn);

    /**
     * Закрепить/открепить комментарий.
     *
     * @param userId  id инициатора события
     * @param eventId id события
     * @param commId  id комментария
     * @param pinned  true - закрепить, false - открепить
     * @return обновленный комментарий
     */
    CommentDtoOut pin(Long userId,
                      Long eventId,
                      Long commId,
                      Boolean pinned);
}
