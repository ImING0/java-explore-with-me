package ru.practicum.ewm.category.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDtoIn;
import ru.practicum.ewm.category.dto.CategoryDtoOut;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.admin.ICategoryAdminService;
import ru.practicum.ewm.error.DataConflictException;
import ru.practicum.ewm.error.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class CategoryAdminService implements ICategoryAdminService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDtoOut create(CategoryDtoIn categoryDtoIn) {
        Category categoryToSave = CategoryMapper.toCategory(categoryDtoIn);
        Category saved;
        try {
            saved = categoryRepository.saveAndFlush(categoryToSave);
        } catch (DataIntegrityViolationException ex) {
            throw new DataConflictException(ex.getMessage(), ex.getCause());
        }
        return CategoryMapper.toDtoOut(saved);
    }

    @Override
    @Transactional
    public CategoryDtoOut update(CategoryDtoIn categoryDtoIn,
                                 Long catId) {
        Category existingCategory = getCategoryOrThrow(catId);
        Category categoryToUpdate = CategoryMapper.toCategory(categoryDtoIn);
        categoryToUpdate.setId(existingCategory.getId());
        categoryToUpdate.setCreatedOn(existingCategory.getCreatedOn());
        Category savedCategory;
        try {
            savedCategory = categoryRepository.saveAndFlush(categoryToUpdate);
        } catch (DataIntegrityViolationException ex) {
            throw new DataConflictException(ex.getMessage(), ex.getCause());
        }
        return CategoryMapper.toDtoOut(savedCategory);
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        Category categoryToDelete = getCategoryOrThrow(catId);
        try {
            categoryRepository.delete(categoryToDelete);
            categoryRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new DataConflictException(ex.getMessage(), ex.getCause());
        }
    }

    private Category getCategoryOrThrow(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Category with id " + "%d not found", catId)));
    }

    private Category getCategoryByName(String name) {
        return categoryRepository.getCategoryByNameIgnoreCase(name);
    }
}
