package ru.practicum.publics.category.service;

import ru.practicum.admin.category.dto.CategoryDto;

public interface CategoryPublicService {
    Iterable<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(long id);
}
