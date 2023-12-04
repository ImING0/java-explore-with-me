package ru.practicum.ewm.event.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDtoOut;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserShortDtoOut;
import ru.practicum.ewm.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class EventFullDtoOut {
    /* Общая информация о событии*/

    private Long id;
    private String title;
    private String annotation;
    private String description;
    private UserShortDtoOut initiator;
    private CategoryDtoOut category;
    private Integer participantLimit;
    private EventState state;
    private Long views;
    private Long confirmedRequests; //Количество одобренных заявок на участие в данном событии
    /*Даты*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    private LocalDateTime eventDate; //Дата события
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    private LocalDateTime createdOn; //Дата создания
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    private LocalDateTime publishedOn; //Дата публикации
    /*Координаты проведения события*/
    private LocationDto location;
    /*Булевы значения*/
    private Boolean paid;
    private Boolean requestModeration;
    /*Комментарии*/
    private Set<Comment> comments;
}
