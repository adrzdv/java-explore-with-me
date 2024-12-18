package ru.practicum.enw.model.mapper.custom;

import ru.practicum.enw.model.entity.LocationDto;
import ru.practicum.enw.model.entity.LocationEwm;

public class LocationMapper {
    public static LocationDto toDto(LocationEwm locationEwm) {
        return new LocationDto(locationEwm.getLat(), locationEwm.getLon());
    }

}
