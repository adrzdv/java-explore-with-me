package ru.practicum.enw.model.event;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.enw.model.entity.LocationEwm;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotBlank(message = "Error: Title is required. Value: ${validatedValue}")
    @Size(min = 3, max = 120, message = "Error: Title length must be between 3 and 120 characters. " +
            "Value: ${validatedValue}")
    private String title;

    @NotBlank(message = "Error: Annotation is required")
    @Size(min = 20, max = 2000, message = "Error: Annotation length must be between 20 and 2000 characters. " +
            "Value: ${validatedValue}")
    private String annotation;

    @NotNull(message = "Error: Category is required. Value: ${validatedValue}")
    private Long category;

    @NotBlank(message = "Error: Description is required. Value: ${validatedValue}")
    @Size(min = 20, max = 7000, message = "Error: Description length must be between 20 and 7000 characters. " +
            "Value: ${validatedValue}")
    private String description;

    @NotNull(message = "Error: Event date must be specified. Value: ${validatedValue}")
    private String eventDate;

    @NotNull(message = "Error: Location must be specified. Value: ${validatedValue}")
    private LocationEwm location;
    private Boolean paid;

    @Min(value = 0, message = "Error: Participant limit must be positive. Value: ${validatedValue}")
    private Integer participantLimit;
    private Boolean requestModeration;

}
