package ru.practicum.category.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.admin.AdminCategoryService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Получен HTTP-запрос на создание категории: {}", newCategoryDto);
        CategoryDto createdCategory = adminCategoryService.create(newCategoryDto);
        log.info("Успешно обработан HTTP-запрос на создание категории: {}", newCategoryDto);
        return createdCategory;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long catId) {
        log.info("Получен HTTP-запрос на удаление категории с id = {}", catId);
        adminCategoryService.delete(catId);
        log.info("Успешно обработан HTTP-запрос на удаление категории с id = {}", catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable @Positive Long catId, @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Получен HTTP-запрос на обновление категории: {}", categoryDto);
        CategoryDto updatedCategory = adminCategoryService.update(catId, categoryDto);
        log.info("Успешно обработан HTTP-запрос на обновление категории: {}", categoryDto);
        return updatedCategory;
    }
}
