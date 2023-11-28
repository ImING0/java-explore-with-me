package ru.practicum.ewm.event.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.request.dto.RequestDtoOut;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdDtoOut {
    private List<RequestDtoOut> confirmedRequests;
    private List<RequestDtoOut> rejectedRequests;
}
