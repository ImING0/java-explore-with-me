package ru.practicum.ewm.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Найти всех пользователей по списку id
     *
     * @param ids      список id
     * @param pageable параметры пагинации
     * @return список пользователей
     */
    List<User> findAllByIdIn(List<Long> ids,
                             Pageable pageable);
}
