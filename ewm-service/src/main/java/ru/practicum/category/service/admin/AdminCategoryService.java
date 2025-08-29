package ru.practicum.category.service.admin;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

public interface AdminCategoryService {

    CategoryDto create(NewCategoryDto newCategoryDto);

    void delete(Long catId);

    CategoryDto update(CategoryDto categoryDto);
}