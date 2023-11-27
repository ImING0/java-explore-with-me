package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.request.model.RequestStatus;

import java.time.LocalDateTime;

/**
 * DTO запроса на участие в мероприятии исходящий
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDtoOut {
    private Long id;
    private Long event;
    private Long requester;
    private RequestStatus status;
    private LocalDateTime created;
}
