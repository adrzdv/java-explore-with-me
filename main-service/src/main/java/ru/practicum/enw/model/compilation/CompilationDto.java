package ru.practicum.enw.model.compilation;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Error: must not be blank. Value: ${validateValue}")
    @Size(min = 1, max = 50, message = "Error: Title length must be between 1 and 50 characters")
    private String title;
    @Nullable
    private List<EventShortDto> events;
    @Nullable
    private Boolean pinned;
}
