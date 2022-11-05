package ru.practicum.publics.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.admin.category.dto.CategoryDto;
import ru.practicum.publics.category.service.CategoryPublicService;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryPublicService categoryPublicService;

    @GetMapping
    public Iterable<CategoryDto> getCategories(@RequestParam(defaultValue = "0", required = false) int from,
                                               @RequestParam(defaultValue = "10", required = false) int size) {
        return categoryPublicService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        return categoryPublicService.getCategory(catId);
    }
}
