package ru.practicum.ewm.comment.service.admin;

import ru.practicum.ewm.comment.dto.CommentDtoIn;
import ru.practicum.ewm.comment.dto.CommentDtoOut;

/**
 * Сервис для работы с комментариями в роли администратора.
 */
public interface IAdminCommentService {
    /**
     * Обновление комментария.
     *
     * @param eventId      идентификатор события
     * @param commId       идентификатор комментария
     * @param commentDtoIn новые данные комментария
     * @return обновленный комментарий
     */
    CommentDtoOut update(Long eventId,
                         Long commId,
                         CommentDtoIn commentDtoIn);

    /**
     * Удаление комментария (скрытие от пользователей).
     *
     * @param eventId идентификатор события
     * @param commId  идентификатор комментария
     * @return удаленный комментарий
     */
    CommentDtoOut delete(Long eventId,
                         Long commId);
}
