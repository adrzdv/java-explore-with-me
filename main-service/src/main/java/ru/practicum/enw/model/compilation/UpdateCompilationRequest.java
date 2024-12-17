package ru.practicum.enw.model.compilation;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    @NotBlank(message = "Error: must not be blank. Value: ${validateValue}")
    @Size(min = 1, max = 50, message = "Error: Title length must be between 1 and 50 characters")
    private String title;
    @Nullable
    private Boolean pinned;
    @Nullable
    private List<Long> events;
}
