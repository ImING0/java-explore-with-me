package ru.practicum.ewm.comment.model;

import lombok.Getter;

/**
 * Состояние комментария
 */
@Getter
public enum CommentState {
    DELETED_BY_USER("Удалено пользователем"),
    DELETED_BY_ADMIN("Удалено администратором"),
    VISIBLE("Видимый");

    private final String description;

    CommentState(String description) {
        this.description = description;
    }
}
