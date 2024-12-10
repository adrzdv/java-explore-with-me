package ru.practicum.enw.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NewUserRequest {
    @NotBlank(message = "Error: must not be blank. Value: ${validatedValue}")
    @Size(message = "Error: length must be between 6 and 254 characters. Value: ${validatedValue}", min = 6, max = 254)
    private String name;
    @NotBlank(message = "Error: must not be blank. Value: ${validatedValue}")
    @Email(message = "Error: email has invalid format. Value: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @Size(message = "Error: length must be between 2 and 250 characters", min = 2, max = 250)
    private String email;
}
