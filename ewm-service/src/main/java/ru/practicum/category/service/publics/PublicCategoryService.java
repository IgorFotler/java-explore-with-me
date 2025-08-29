package ru.practicum.category.service.publics;

import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryDto> findAll(int from, int size);

    CategoryDto findById(Long catId);
}
