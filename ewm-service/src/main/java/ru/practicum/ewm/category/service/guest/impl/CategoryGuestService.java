package ru.practicum.ewm.category.service.guest.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDtoOut;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.guest.ICategoryGuestService;
import ru.practicum.ewm.error.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryGuestService implements ICategoryGuestService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDtoOut> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper::toDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDtoOut getById(Long catId) {
        return CategoryMapper.toDtoOut(getCategoryOrThrow(catId));
    }

    private Category getCategoryOrThrow(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Category with id " + "%d not found", catId)));
    }
}
