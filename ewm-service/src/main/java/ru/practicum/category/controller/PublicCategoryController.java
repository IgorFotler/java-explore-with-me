package ru.practicum.category.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.publics.PublicCategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {

    private final PublicCategoryService publicCategoryService;

    @GetMapping
    public List<CategoryDto> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен HTTP-запрос на получение всех категорий " +
                "from = {}," +
                "size = {}", from, size);
        List<CategoryDto> categoryes = publicCategoryService.findAll(from, size);
        log.info("Успешно обработан HTTP-запрос на получение всех категорий " +
                "from = {}," +
                "size = {}", from, size);
        return categoryes;
    }

    @GetMapping("/{catId}")
    public CategoryDto findById(@PathVariable @Positive Long catId) {
        log.info("Получен HTTP-запрос на получение категории по id: {}", catId);
        CategoryDto categoryDto = publicCategoryService.findById(catId);
        log.info("Успешно обработан HTTP-запрос на получение категории по id: {}", catId);
        return categoryDto;
    }
}