package ru.practicum.ewm.event.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDtoOut;
import ru.practicum.ewm.user.dto.UserShortDtoOut;
import ru.practicum.ewm.util.DateTimeUtil;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDtoOut {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDtoOut category;
    private UserShortDtoOut initiator;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    private Long confirmedRequests;
    private Long views;
    private Boolean paid;
}
