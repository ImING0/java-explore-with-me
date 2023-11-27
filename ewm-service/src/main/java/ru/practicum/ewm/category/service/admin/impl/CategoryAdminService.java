package ru.practicum.ewm.category.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDtoIn;
import ru.practicum.ewm.category.dto.CategoryDtoOut;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.admin.ICategoryAdminService;
import ru.practicum.ewm.error.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class CategoryAdminService implements ICategoryAdminService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDtoOut create(CategoryDtoIn categoryDtoIn) {
        Category categoryToSave = CategoryMapper.toCategory(categoryDtoIn);
        return CategoryMapper.toDtoOut(categoryRepository.save(categoryToSave));
    }

    @Override
    public CategoryDtoOut update(CategoryDtoIn categoryDtoIn,
                                 Long catId) {
        Category existingCategory = getCategoryOrThrow(catId);
        Category categoryToUpdate = CategoryMapper.toCategory(categoryDtoIn);
        categoryToUpdate.setId(existingCategory.getId());
        categoryToUpdate.setCreatedOn(existingCategory.getCreatedOn());
        return CategoryMapper.toDtoOut(categoryRepository.save(categoryToUpdate));
    }

    @Override
    public void delete(Long catId) {
        Category categoryToDelete = getCategoryOrThrow(catId);
        categoryRepository.delete(categoryToDelete);
    }

    private Category getCategoryOrThrow(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Category with id " + "%d not found", catId)));
    }
}
