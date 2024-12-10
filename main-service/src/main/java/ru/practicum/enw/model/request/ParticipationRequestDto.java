package ru.practicum.enw.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestDto {

    private long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @NotNull
    private long event;
    @NotNull
    private long requester;
    @NotBlank
    @Pattern(regexp = "REJECTED|CONFIRMED", message = "Error: Status must be REJECTED|CONFIRMED")
    private String status;
}
