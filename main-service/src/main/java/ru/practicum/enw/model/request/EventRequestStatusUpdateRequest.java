package ru.practicum.enw.model.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    @Pattern(regexp = "CONFIRMED|REJECTED",
            message = "Error: State must be in: CONFIRMED|REJECTED")
    String status;
}
