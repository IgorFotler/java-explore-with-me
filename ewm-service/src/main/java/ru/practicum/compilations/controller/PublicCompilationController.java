package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.service.publics.PublicCompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Получен HTTP-запрос на получение всех подборок " +
                "pinned = {}, " +
                "from = {}, " +
                "size = {}", pinned, from, size);
        List<CompilationDto> compilations = publicCompilationService.findAll(pinned, from, size);
        log.info("Успешно обработан HTTP-запрос на получение всех подборок " +
                "pinned = {}, " +
                "from = {}, " +
                "size = {}", pinned, from, size);
        return compilations;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Получен HTTP-запрос на получение подборки по id: {}", compId);
        CompilationDto compilationDto = publicCompilationService.findById(compId);
        log.info("Успешно обработан HTTP-запрос на получение подборки по id: {}", compId);
        return compilationDto;
    }
}