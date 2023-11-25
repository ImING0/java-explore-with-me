package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDtoIn {
    @NotNull(message = "Latitude must not be null")
    private Float lat; // Широта

    @NotNull(message = "Longitude must not be null")
    private Float lon; // Долгота
}
