package ru.practicum.enw.service.location;

import ru.practicum.enw.model.entity.LocationEwm;

public interface LocationService {

    /**
     * Add a new location
     *
     * @param location new location
     * @return LocationEwm object
     */
    LocationEwm add(LocationEwm location);

    /**
     * Get an existing location by id
     *
     * @param id identification number
     * @return LocationEwm object
     */
    LocationEwm get(long id);
}
