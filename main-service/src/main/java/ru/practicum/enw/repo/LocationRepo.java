package ru.practicum.enw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.enw.model.entity.LocationEwm;

public interface LocationRepo extends JpaRepository<LocationEwm, Long> {
}
