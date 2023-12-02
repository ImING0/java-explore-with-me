package ru.practicum.ewm.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    /**
     * Найти всех пользователей по списку id
     *
     * @param ids      список id
     * @param pageable параметры пагинации
     * @return список пользователей
     */
    List<User> findAllByIdIn(Set<Long> ids,
                             Pageable pageable);
}
