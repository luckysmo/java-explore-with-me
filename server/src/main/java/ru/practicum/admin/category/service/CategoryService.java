package ru.practicum.admin.category.service;

import ru.practicum.admin.category.dto.CategoryDto;

public interface CategoryService {
    CategoryDto saveCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(Long id);
}
