package ru.practicum.enw.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.entity.User;
import ru.practicum.enw.model.user.NewUserRequest;
import ru.practicum.enw.model.user.UserDto;
import ru.practicum.enw.model.mapper.custom.UserMapper;
import ru.practicum.enw.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {

    private UserRepo repository;

    @Override
    @Transactional
    public UserDto add(NewUserRequest userNewDto) {

        User user = UserMapper.fromNewDtoToEntity(userNewDto);

        return UserMapper.fromEntityToDto(repository.save(user));
    }

    @Override
    @Transactional
    public void delete(long id) throws NotFoundCustomException {

        if (repository.findById(id).isEmpty()) {

            throw new NotFoundCustomException("User with id=" + id + " not found");
        }

        repository.deleteById(id);
    }

    @Override
    public List<UserDto> get(List<Integer> ids, Integer from, Integer size) {

        List<UserDto> res = new ArrayList<>();

        if (ids != null) {

            if (from != null && size != null) {
                res = repository.getUsersWithIdsAndLimit(ids, from, size).stream()
                        .map(UserMapper::fromEntityToDto)
                        .toList();
            } else {
                res = repository.getUsersWithIds(ids).stream()
                        .map(UserMapper::fromEntityToDto)
                        .toList();
            }

        } else if (from != null && size != null) {

            res = repository.getUsersWithLimit(from, size).stream()
                    .map(UserMapper::fromEntityToDto)
                    .toList();

        }

        return res;
    }

    @Override
    public UserDto getById(long id) {

        return UserMapper.fromEntityToDto(repository.findById(id).orElseThrow());
    }
}
