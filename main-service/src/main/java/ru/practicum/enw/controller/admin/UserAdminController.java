package ru.practicum.enw.controller.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.user.NewUserRequest;
import ru.practicum.enw.model.user.UserDto;
import ru.practicum.enw.service.user.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserAdminController {

    private final UserService service;

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) throws NotFoundCustomException {
        service.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest user) {

        return service.add(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(@RequestParam(required = false, defaultValue = "0") int from,
                                  @RequestParam(required = false, defaultValue = "10") int size,
                                  @RequestParam(required = false) List<Long> ids) {


        return service.get(ids, from, size);
    }

}
