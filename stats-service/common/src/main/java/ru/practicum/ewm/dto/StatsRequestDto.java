package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}
