package ru.practicum.compilations.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.service.admin.AdminCompilationServiceImpl;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final AdminCompilationServiceImpl adminCompilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Получен HTTP-запрос на создание подборки: {}", newCompilationDto);
        CompilationDto createdCompilation = adminCompilationService.create(newCompilationDto);
        log.info("Успешно обработан HTTP-запрос на создание подборки: {}", newCompilationDto);
        return createdCompilation;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Получен HTTP-запрос на удаление подборки с id = {}", compId);
        adminCompilationService.delete(compId);
        log.info("Успешно обработан HTTP-запрос на удаление подборки с id = {}", compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable Long compId,
            @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Получен HTTP-запрос на обновление подборки: {}", updateCompilationRequest);
        CompilationDto updatedCompilation = adminCompilationService.update(compId, updateCompilationRequest);
        log.info("Успешно обработан HTTP-запрос на обновление подборки: {}", updateCompilationRequest);
        return updatedCompilation;
    }
}