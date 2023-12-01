package ru.practicum.ewm.user.service.admin;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.user.dto.UserDtoIn;
import ru.practicum.ewm.user.dto.UserDtoOut;

import java.util.List;
import java.util.Set;

/**
 * Сервис для администрирования пользователей
 */
public interface IUserAdminService {
    /**
     * Создание пользователя
     *
     * @param userDtoIn данные пользователя
     * @return сохраненный пользователь
     */
    UserDtoOut create(UserDtoIn userDtoIn);

    /**
     * Удаление пользователя
     *
     * @param userId идентификатор пользователя
     */
    void delete(Long userId);

    /**
     * Получение списка пользователей
     *
     * @param ids      идентификаторы пользователей
     * @param pageable параметры пагинации
     * @return список пользователей
     */
    List<UserDtoOut> getAll(Set<Long> ids,
                            Pageable pageable);
}
