package ru.practicum.enw.model.compilation;

import jakarta.annotation.Nullable;
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
    @Nullable
    @Size(min = 1, max = 50, message = "Error: Title must be between 1 and 50 characters")
    private String title;
    @Nullable
    private Boolean pinned;
    @Nullable
    private List<Long> events;
}
