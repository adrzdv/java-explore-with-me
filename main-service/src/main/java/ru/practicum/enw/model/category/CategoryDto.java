package ru.practicum.enw.model.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private long id;
    @NotBlank(message = "Error: must not be blank. Value: ${validatedValue}")
    @Size(message = "Error: length must be between 1 and 50 characters. Value: ${validatedValue}", min = 1, max = 50)
    private String name;


}
