package ru.practicum.enw.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


/**
 * Base class for event object
 * title - required field, mixLength = 20, maxLength = 120
 * annotation - required field, minLength = 20, maxLength = 2000
 * category - required field,
 * description - required field, minLength = 20, maxLength = 7000
 * eventDate - required field, pattern = "yyyy-MM-dd HH:mm:ss"
 * paid - default value = false
 * participantLimit - default value = 0
 * requestModeration - default value = true
 */

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category")
    private Category category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator")
    private User initiator;

    @OneToOne
    @JoinColumn(name = "location")
    private LocationEwm location;

    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    private Integer confirmedRequests = 0;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private String state;
    private Integer views;

}
