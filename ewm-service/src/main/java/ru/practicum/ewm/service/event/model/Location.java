package ru.practicum.ewm.service.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {
    @Column(name = "lat", nullable = false)
    private Float lat; //Широта
    @Column(name = "lon", nullable = false)
    private Float lon; //долгота
}
