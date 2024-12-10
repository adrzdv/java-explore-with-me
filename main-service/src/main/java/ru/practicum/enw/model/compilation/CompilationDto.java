package ru.practicum.enw.model.compilation;

import lombok.*;
import ru.practicum.enw.model.event.EventShortDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {
    private long id;
    private String title;
    private List<EventShortDto> events;
    private Boolean pinned;
}
