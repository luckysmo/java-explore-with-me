package ru.practicum.admin.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.admin.category.Category;
import ru.practicum.exceptions.CategoryNotFoundException;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    default Category checkAndReturnCategoryIfExist(Long id) {
        return findById(id).orElseThrow(
                () -> new CategoryNotFoundException(String.format("Категория с id=%s не найдена", id)));
    }
}
