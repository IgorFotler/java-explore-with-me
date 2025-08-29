package ru.practicum.events.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.service.admin.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {

    private final AdminEventService adminEventService;

    @GetMapping
    public List<EventFullDto> searchEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        log.info("Получен HTTP-запрос на получение событий с параметрами: users = {}, states = {}, categories = {}, rangeStart = {}, "
                + "rangeEnd = {}, from = {}, size = {}, request = {}", users, states, categories, rangeStart, rangeEnd, from, size, request);

        List<EventFullDto> events = adminEventService.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size, request);

        log.info("Успешно обработан HTTP-запрос на получение событий с параметрами: users = {}, states = {}, categories = {}, rangeStart = {}, "
                + "rangeEnd = {}, from = {}, size = {}, request = {}", users, states, categories, rangeStart, rangeEnd, from, size, request);

        return events;
    }

    @PatchMapping("{eventId}")
    public EventFullDto updateByAdmin(@PathVariable @Positive Long eventId,
                                  @RequestBody @Valid UpdateEventAdminRequest updateRequest) {
        log.info("Получен HTTP-запрос на обновление события с id = {}, updateRequest = {}", eventId, updateRequest);
        EventFullDto event = adminEventService.updateByAdmin(eventId, updateRequest);
        log.info("Успешно обработан HTTP-запрос на обновление события с id = {}, updateRequest = {}", eventId, updateRequest);
        return event;
    }
}
