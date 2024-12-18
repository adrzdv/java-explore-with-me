package ru.practicum.enw.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserShortDto {
    private long id;
    private String name;
}
