package ru.practicum.requests.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestsController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable @Positive Long userId,
                                          @RequestParam @Positive Long eventId) {
        log.info("Получен HTTP-запрос на создание запроса с userId = {}, eventId = {}", userId, eventId);
        ParticipationRequestDto participationRequestDto = requestService.create(userId, eventId);
        log.info("Успешно обработан HTTP-запрос на создание запроса с userId = {}, eventId = {}", userId, eventId);
        return participationRequestDto;
    }

    @GetMapping
    public List<ParticipationRequestDto> getUserRequests(@PathVariable @Positive Long userId) {
        log.info("Получен HTTP-запрос на получение запросов пользователя с userId = {}", userId);
        List<ParticipationRequestDto> requests = requestService.getUserRequests(userId);
        log.info("Успешно обработан HTTP-запрос на получение запросов пользователя с userId = {}", userId);
        return requests;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelUserRequest(@PathVariable @Positive Long userId,
                                                   @PathVariable @Positive Long requestId) {
        log.info("Получен HTTP-запрос на отмену запроса с userId = {}, requestId = {}", userId, requestId);
        ParticipationRequestDto participationRequestDto = requestService.cancelUserRequest(userId, requestId);
        log.info("Успешно обработан HTTP-запрос на отмену запроса с userId = {}, requestId = {}", userId, requestId);
        return participationRequestDto;
    }
}