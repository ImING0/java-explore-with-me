package ru.practicum.ewm.event.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdDtoIn {
    @NotEmpty(message = "Request ids must not be empty")
    private Set<Long> requestIds;
    @NotNull
    private RequestStatusUserUpdDtoIn status;
}
