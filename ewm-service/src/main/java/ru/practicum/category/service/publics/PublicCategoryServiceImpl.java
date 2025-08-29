package ru.practicum.category.service.publics;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> findAll(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return categoryRepository
                .findAll(pageRequest)
                .stream()
                .map(CategoryMapper::convertToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + catId + " не найдена"));
        return CategoryMapper.convertToCategoryDto(category);
    }
}

