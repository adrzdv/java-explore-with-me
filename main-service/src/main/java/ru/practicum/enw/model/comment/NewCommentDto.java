package ru.practicum.enw.model.comment;

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
public class NewCommentDto {
    @NotBlank(message = "Error. Comment text can't be blank")
    @Size(min = 1, max = 7000, message = "Error. Comment text must be between 1 and 7000 characters.")
    private String text;
}
