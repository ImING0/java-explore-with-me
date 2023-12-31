package ru.practicum.ewm.stats.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "ru.practicum.ewm")
@Slf4j
public class DefExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(ResourceNotFoundException ex) {
        log.error(ex.getMessage());
        return ApiError.buildFromExAndHttpStatus(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {DataConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataConflictException(DataConflictException ex) {
        log.error(ex.getMessage());
        return ApiError.buildFromExAndHttpStatus(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(BadRequestException ex) {
        log.error(ex.getMessage());
        return ApiError.buildFromExAndHttpStatus(ex, HttpStatus.BAD_REQUEST);
    }
}
