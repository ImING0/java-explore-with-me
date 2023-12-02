package ru.practicum.ewm.stats.error;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import ru.practicum.ewm.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
@Data
public class ApiError {
    private List<String> errors; // Список стектрейсов или описания ошибок
    private String message; // Сообщение об ошибке
    private String reason; //Общее описание причины ошибки
    private Integer httpStatus; // Статус ошибки
    private String timestamp; // Время возникновения ошибки

    /**
     * Создает объект ошибки из исключения и статуса ошибки
     *
     * @param ex         исключение
     * @param httpStatus статус ошибки
     * @return объект ошибки
     */
    public static ApiError buildFromExAndHttpStatus(Throwable ex,
                                                    HttpStatus httpStatus) {
        List<String> stackTrace = Stream.of(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
        String message = ex.getMessage();
        String reason = ex.getCause() != null ? ex.getCause()
                .toString() : "Неизвестная причина";
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
