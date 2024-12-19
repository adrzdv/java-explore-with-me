package ru.practicum.enw.service.location;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enw.model.entity.LocationEwm;
import ru.practicum.enw.repo.LocationRepo;

@Service
@AllArgsConstructor
public class LocationServiceImp implements LocationService {

    private final LocationRepo repo;

    @Override
    @Transactional
    public LocationEwm add(LocationEwm location) {

        return repo.save(location);
    }

    @Override
    public LocationEwm get(long id) {

        return repo.findById(id).orElseThrow();
    }

}
