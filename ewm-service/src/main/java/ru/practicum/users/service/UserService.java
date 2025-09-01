package ru.practicum.users.service;

import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(NewUserRequest newUserRequest);

    List<UserDto> findUsers(List<Long> ids, int from, int size);

    void delete(Long userId);
}