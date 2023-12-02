package ru.practicum.ewm.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.event.dto.event.LocationDto;
import ru.practicum.ewm.event.model.Location;

/**
 * Маппер для Location
 */
@UtilityClass
public class LocationMapper {
    /**
     * Метод преобразования объекта Location в LocationDto
     *
     * @param location - объект Location
     * @return объект LocationDto
     */
    public LocationDto toDtoOut(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
