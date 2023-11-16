package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.util.DateTimeUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


/**
 * DTO для сохранения статистики
 */
@Data
@Builder
public class StatsRequestDto {
    @NotBlank(message = "The app cannot be empty")
    private String app;
    @NotBlank(message = "The uri cannot be empty")
    private String uri;
    @NotBlank(message = "The ip cannot be empty")
    private String ip;
    @NotNull(message = "The timestamp cannot be null")
    @JsonFormat(pattern = DateTimeUtil.DATETIME_FORMAT)
    private LocalDateTime timeStamp;
}
