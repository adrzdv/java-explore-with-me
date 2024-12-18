package ru.practicum.enw.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private Boolean pinned;
    @ManyToMany
    @JoinTable(name = "compilation_list",
    joinColumns = @JoinColumn(name = "id_compilation"),
    inverseJoinColumns = @JoinColumn(name = "id_event"))
    private List<Event> events;

}
