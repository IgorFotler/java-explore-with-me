package ru.practicum.category.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName().isBlank()) {
            throw new BadRequestException("Поле name не может быть пустым");
        }

        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException("Категория с name = " + newCategoryDto.getName() + " уже существует");
        }

        Category createdCategory = categoryRepository.save(CategoryMapper.convertToCategory(newCategoryDto));
        return CategoryMapper.convertToCategoryDto(createdCategory);
    }

    @Override
    public void delete(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Категория с id = " + catId + " не найдена");
        }

        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("Невозможно удалить категорию, к которой привязаны события");
        }

        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto update(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + catId + " не найдена"));

        if (category.getName().equalsIgnoreCase(categoryDto.getName())) {
            return CategoryMapper.convertToCategoryDto(category);
        }

        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException("Категория с name = " + categoryDto.getName() + " уже существует");
        }

        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.convertToCategoryDto(updatedCategory);
    }
}

