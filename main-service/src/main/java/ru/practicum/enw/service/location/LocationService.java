package ru.practicum.enw.service.location;

import ru.practicum.enw.model.entity.LocationEwm;

public interface LocationService {

    LocationEwm add(LocationEwm location);

    LocationEwm get(long id);
}
