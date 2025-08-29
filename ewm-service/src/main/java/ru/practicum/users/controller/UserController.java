package ru.practicum.users.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", newUserRequest);
        UserDto createdUser = userService.create(newUserRequest);
        log.info("Успешно обработан HTTP-запрос на создание пользователя: {}", newUserRequest);
        return createdUser;
    }

    @GetMapping
    public List<UserDto> findUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Получен HTTP-запрос на получение списка пользователей с ids = {}, from = {}, size = {}", ids, from, size);
        List<UserDto> users = userService.findUsers(ids, from, size);
        log.info("Успешно обработан HTTP-запрос на получение списка пользователей с ids = {}, from = {}, size = {}", ids, from, size);
        return users;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long userId) {
        log.info("Получен HTTP-запрос на удаление пользователя по id: {}", userId);
        userService.delete(userId);
        log.info("Успешно обработан HTTP-запрос на удаление пользователя по id: {}", userId);
    }
}
