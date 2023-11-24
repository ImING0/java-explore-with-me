package ru.practicum.ewm.service.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.service.category.dto.CategoryDtoIn;
import ru.practicum.ewm.service.category.dto.CategoryDtoOut;

import java.util.List;

/**
 * Сервис для работы с категориями
 */
public interface ICategoryService {
    /**
     * Создание категории
     *
     * @param categoryDtoIn dto для создания категории
     * @return dto созданной категории
     */
    CategoryDtoOut create(CategoryDtoIn categoryDtoIn);

    /**
     * Обновление категории
     *
     * @param categoryDtoIn dto для обновления категории
     * @param catId
     * @return dto обновленной категории
     */
    CategoryDtoOut update(CategoryDtoIn categoryDtoIn,
                          Long catId);

    /**
     * Получение всех категорий
     *
     * @param pageable параметры пагинации
     * @return список категорий
     */
    List<CategoryDtoOut> getAll(Pageable pageable);

    /**
     * Получение категории по идентификатору
     *
     * @param catId идентификатор категории
     * @return dto категории
     */
    CategoryDtoOut getById(Long catId);

    /**
     * Удаление категории
     *
     * @param catId идентификатор категории
     */
    void delete(Long catId);
}
