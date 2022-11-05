package ru.practicum.admin.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.admin.category.Category;
import ru.practicum.admin.category.dto.CategoryDto;
import ru.practicum.admin.category.repository.CategoryRepository;

import static ru.practicum.admin.category.dto.CategoryMapper.toCategory;
import static ru.practicum.admin.category.dto.CategoryMapper.toCategoryDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        log.debug("Запрос saveCategory по name - {}", categoryDto.getName());
        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        log.debug("Запрос updateCategory по id - {} и name - {}", categoryDto.getId(), categoryDto.getName());
        categoryRepository.checkAndReturnCategoryIfExist(categoryDto.getId());
        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    @Override
    public void deleteCategory(Long id) {
        log.debug("Запрос deleteCategory по id - {}", id);
        Category category = categoryRepository.checkAndReturnCategoryIfExist(id);
        categoryRepository.delete(category);
    }
}
