package ru.practicum.enw.model.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {

    @NotBlank(message = "Error: must not be blank. Value: ${validatedValue}")
    @Size(message = "Error: length must be between 1 and 50 characters. Value: ${validatedValue}", min = 1, max = 50)
    private String name;
}
