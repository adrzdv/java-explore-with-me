package ru.practicum.enw.model.mapper.custom;

import org.springframework.stereotype.Component;
import ru.practicum.enw.model.entity.User;
import ru.practicum.enw.model.user.NewUserRequest;
import ru.practicum.enw.model.user.UserDto;
import ru.practicum.enw.model.user.UserShortDto;

@Component
public class UserMapper {

    public static UserDto fromNewDto(NewUserRequest user) {

        return new UserDto(0, user.getName(), user.getEmail());
    }

    public static UserShortDto toShortDto(UserDto user) {

        return new UserShortDto(user.getId(), user.getName());
    }

    public static User fromNewDtoToEntity(NewUserRequest user) {

        return new User(0, user.getName(), user.getEmail());
    }

    public static UserShortDto fromEntityToShortDto(User user) {

        return new UserShortDto(user.getId(), user.getName());
    }

    public static UserDto fromEntityToDto(User user) {

        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User fromDtoToEntity(UserDto user) {

        return new User(user.getId(), user.getName(), user.getEmail());
    }
}
