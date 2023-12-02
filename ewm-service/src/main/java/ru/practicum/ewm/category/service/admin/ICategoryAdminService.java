package ru.practicum.ewm.category.service.admin;

import ru.practicum.ewm.category.dto.CategoryDtoIn;
import ru.practicum.ewm.category.dto.CategoryDtoOut;

/**
 * Сервис для работы с категориями от имени админа
 */
public interface ICategoryAdminService {
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
     * @param catId         идентификатор категории
     * @return dto обновленной категории
     */
    CategoryDtoOut update(CategoryDtoIn categoryDtoIn,
                          Long catId);

    /**
     * Удаление категории
     *
     * @param catId идентификатор категории
     */
    void delete(Long catId);
}
