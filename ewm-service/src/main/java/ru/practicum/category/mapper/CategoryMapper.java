package ru.practicum.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

@Component
public class CategoryMapper {

    public static Category convertToCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public static CategoryDto convertToCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
