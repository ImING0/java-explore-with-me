package ru.practicum.ewm.category.service.guest;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.category.dto.CategoryDtoOut;

import java.util.List;

/**
 * Сервис для работы с категориями для всех пользователей
 */
public interface ICategoryGuestService {
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
}
