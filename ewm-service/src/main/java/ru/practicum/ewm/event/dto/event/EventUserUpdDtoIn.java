package ru.practicum.ewm.event.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.util.DateTimeUtil;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * DTO для обновления события пользователем с ролью пользователь
 * если поле не передано, то оно не будет обновлено
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventUserUpdDtoIn {
    @Size(min = 3, max = 120, message = "Title must be between 3 and 120 characters")
    private String title;
    @Size(min = 20, max = 2000)
    private String annotation;
    @Size(min = 20, max = 7000)
    private String description;
    private Long category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    @Future
    private LocalDateTime eventDate;
    private LocationDto location;
    private StateUserAction stateAction; // что сделать с событием
    private Boolean paid;
    private Boolean requestModeration;
    private Integer participantLimit;
}
