package ru.practicum.enw.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    @NotBlank(message = "Error: must not be blank. Value: ${validatedValue}")
    @Size(message = "Error: length must be between 6 and 254 characters. Value: ${validatedValue}", min = 6, max = 254)
    private String name;
    @NotBlank(message = "Error: must not be blank. Value: ${validatedValue}")
    @Email(message = "Email has invalid format. Value: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @NotBlank(message = "Error: must not be blank. Value: ${validatedValue}")
    @Size(message = "Error: length must be between 2 and 250 characters. Value: ${validatedValue}", min = 2, max = 250)
    private String email;
}
