package ru.practicum.ewm.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.event.dto.LocationDtoOut;
import ru.practicum.ewm.event.model.Location;

/**
 * Маппер для Location
 */
@UtilityClass
public class LocationMapper {
    /**
     * Метод преобразования объекта Location в LocationDtoOut
     *
     * @param location - объект Location
     * @return объект LocationDtoOut
     */
    public LocationDtoOut toDtoOut(Location location) {
        return LocationDtoOut.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
