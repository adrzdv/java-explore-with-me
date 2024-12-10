package ru.practicum.enw.service.user;

import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.user.NewUserRequest;
import ru.practicum.enw.model.user.UserDto;

import java.util.List;

public interface UserService {

    /**
     * Add new user
     *
     * @param user NewUserRequest object
     * @return EserEwm object
     */
    UserDto add(NewUserRequest user);

    /**
     * Delete an existing user
     *
     * @param id identification number of user
     * @throws NotFoundCustomException
     */
    void delete(long id) throws NotFoundCustomException;

    /**
     * Get list of users according having parameters
     *
     * @param ids  List of identification number of users
     * @param from the number of skipped elements to form the list
     * @param size list size of output list
     * @return
     */
    List<UserDto> get(List<Integer> ids, Integer from, Integer size);

    /**
     * Get a User by ID
     *
     * @param id identification number
     * @return UserDto object
     */
    UserDto getById(long id);
}
