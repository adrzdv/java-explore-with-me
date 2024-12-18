package ru.practicum.enw.model.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"lat", "lon"})
@AllArgsConstructor
public class LocationDto {
    @NotNull
    private double lat;
    @NotNull
    private double lon;
}
