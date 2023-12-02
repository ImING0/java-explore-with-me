package ru.practicum.ewm.event.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    @NotNull(message = "Latitude must not be null")
    private Float lat; // Широта

    @NotNull(message = "Longitude must not be null")
    private Float lon; // Долгота
}
