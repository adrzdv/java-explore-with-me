package ru.practicum.enw.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locations")
@Getter
@Setter
@EqualsAndHashCode(of = {"lat", "lon"})
public class LocationEwm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double lat;
    private double lon;
}
