package ru.practicum.events.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.service.publics.PublicEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@Slf4j
public class PublicEventController {

    private final PublicEventService publicEventService;

    @GetMapping
    public List<EventShortDto> getAll(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        log.info("Получен HTTP-запрос на получение событий");

        List<EventShortDto> events = publicEventService.getAll(
                text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request);

        log.info("Успешно обработан HTTP-запрос на получение событий");

        return events;
    }

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен HTTP-запрос на обновление события с id = {}, request = {}", id, request);
        EventFullDto event = publicEventService.getById(id, request);
        log.info("Успешно обработан HTTP-запрос на обновление события с id = {}, request = {}", id, request);
        return event;
    }
}
