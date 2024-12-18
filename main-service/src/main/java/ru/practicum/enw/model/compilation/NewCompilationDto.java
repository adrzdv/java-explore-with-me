package ru.practicum.enw.model.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCompilationDto {

    private List<Long> events;
    private Boolean pinned = false;
    @NotBlank(message = "Error: must not be blank. Value: ${validateValue}")
    @Size(min = 1, max = 50, message = "Error: Title length must be between 1 and 50 characters")
    private String title;

}
