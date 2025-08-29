package ru.practicum.events.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.*;
import ru.practicum.events.service.privates.PrivateEventService;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(
            @PathVariable @Positive Long userId,
            @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Получен HTTP-запрос на создание события с данными: userId {}, newEventDto = {}", userId, newEventDto);
        EventFullDto event = privateEventService.create(userId, newEventDto);
        log.info("Успешно HTTP-запрос на создание события с данными: userId {}, newEventDto = {}", userId, newEventDto);
        return event;
    }

    @GetMapping
    public List<EventShortDto> getEventsByUser(@PathVariable @Positive Long userId,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("Получен HTTP-запрос на получение событий пользователя с " +
                "userId = {}, " +
                "from = {}, " +
                "size = {}", userId, from, size);

        List<EventShortDto> events = privateEventService.getUserEvents(userId, from, size);

        log.info("Успешно обработан HTTP-запрос на получение событий пользователя с " +
                "userId = {}, " +
                "from = {}, " +
                "size = {}", userId, from, size);

        return events;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByUser(@PathVariable @Positive Long userId,
                           @PathVariable @Positive Long eventId,
                           @RequestBody @Valid UpdateEventUserRequest updateRequest) {
        log.info("Получен HTTP-запрос на обновление события с данными " +
                "userId = {}, " +
                "eventId = {}, " +
                "updateRequest = {}", userId, eventId, updateRequest);

        EventFullDto event = privateEventService.updateByUser(userId, eventId, updateRequest);

        log.info("Успешно обработан HTTP-запрос на обновление события с данными " +
                "userId = {}, " +
                "eventId = {}, " +
                "updateRequest = {}", userId, eventId, updateRequest);

        return event;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable @Positive Long userId,
                            @PathVariable @Positive Long eventId) {
        log.info("Получен HTTP-запрос на получение события с eventId = {}, пользователем с userId = {}", eventId, userId);
        EventFullDto event = privateEventService.getById(userId, eventId);
        log.info("Успешно обработан HTTP-запрос на получение события с eventId = {}, пользователем с userId = {}", eventId, userId);
        return event;
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestsForEvents(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long eventId) {
        log.info("Получен HTTP-запрос на получение заявок на участие в собутии с eventId = {}, пользователем с userId = {}", eventId, userId);
        List<ParticipationRequestDto> requests = privateEventService.getAllRequestsForEvents(userId, eventId);
        log.info("Успешно обработан HTTP-запрос на получение заявок на участие в собутии с eventId = {}, пользователем с userId = {}", eventId, userId);
        return requests;
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@RequestBody @Valid EventRequestStatusUpdateRequest updateRequest,
                                                         @PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long eventId) {

        log.info("Получен HTTP-запрос на обновление заявок на участие в событии с данными " +
                "userId = {}, " +
                "eventId = {}, " +
                "updateRequest = {}", userId, eventId, updateRequest);

        EventRequestStatusUpdateResult request = privateEventService.updateRequests(userId, eventId, updateRequest);

        log.info("Получен HTTP-запрос на обновление заявок на участие в событии с данными " +
                "userId = {}, " +
                "eventId = {}, " +
                "updateRequest = {}", userId, eventId, updateRequest);

        return request;
    }
}
