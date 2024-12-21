package ru.practicum.enw.model.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.enw.model.entity.LocationEwm;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    @Nullable
    @Size(min = 20, max = 2000, message = "Error: Annotation length must be between 20 and 2000 characters. " +
            "Value: ${validatedValue}")
    private String annotation;
    @Nullable
    @Size(min = 3, max = 120, message = "Error: Title length must be between 3 and 120 characters. " +
            "Value: ${validatedValue}")
    private String title;
    @Nullable
    @Size(min = 20, max = 7000, message = "Error: Description length must be between 20 and 7000 characters. " +
            "Value: ${validatedValue}")
    private String description;
    @Nullable
    private Long category;
    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;
    @Nullable
    private LocationEwm location;
    @Nullable
    private Boolean paid;
    @Nullable
    @Min(value = 0, message = "Error: Participant limit must be positive. Value: ${validatedValue}")
    private Integer participantLimit;
    @Nullable
    private Boolean requestModeration;
    @Nullable
    @Pattern(regexp = "PUBLISH_EVENT|REJECT_EVENT", message = "Error: StateAction must be: PUBLISH_EVENT|REJECT_EVENT")
    private String stateAction;

}
