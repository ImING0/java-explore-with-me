package ru.practicum.ewm.service.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.service.category.dto.CategoryDtoIn;
import ru.practicum.ewm.service.category.dto.CategoryDtoOut;
import ru.practicum.ewm.service.category.model.Category;

/**
 * Маппер для категорий
 */
@UtilityClass
public class CategoryMapper {
    /**
     * Маппинг из dto в модель
     *
     * @param categoryDtoIn dto для создания категории
     * @return модель категории
     */
    public Category toCategory(CategoryDtoIn categoryDtoIn) {
        return Category.builder()
                .name(categoryDtoIn.getName())
                .build();
    }

    /**
     * Маппинг из модели в dto
     *
     * @param category модель категории
     * @return dto категории
     */
    public CategoryDtoOut toDtoOut(Category category) {
        return CategoryDtoOut.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
