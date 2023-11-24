package ru.practicum.ewm.service.error;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import ru.practicum.ewm.util.DateTimeUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class ApiError {
    private List<String> errors; // Список стектрейсов или описания ошибок
    private String message; // Сообщение об ошибке
    private String reason; //Общее описание причины ошибки
    private Integer httpStatus; // Статус ошибки
    private String timestamp; // Время возникновения ошибки


    public static ApiError buildFromExAndHttpStatus(Throwable ex, HttpStatus httpStatus) {
        List<String> stackTrace = List.of(ex.getStackTrace()).stream()
                .map(StackTraceElement::toString).collect(Collectors.toList());
        String message = ex.getMessage();
        String reason = ex.getCause() != null ? ex.getCause().toString() : "Неизвестная причина";
        Integer httpStatusValue = httpStatus.value();
        String timestamp = DateTimeUtil.getLocalDateTimeAsString(LocalDateTime.now());
        return ApiError.builder()
                .errors(stackTrace)
                .message(message)
                .reason(reason)
                .httpStatus(httpStatusValue)
                .timestamp(timestamp)
                .build();
    }
}
