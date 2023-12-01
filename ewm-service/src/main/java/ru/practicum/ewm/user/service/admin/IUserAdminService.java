package ru.practicum.ewm.user.service.admin;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.user.dto.UserDtoIn;
import ru.practicum.ewm.user.dto.UserDtoOut;

import java.util.List;
import java.util.Set;

public interface IUserAdminService {
    UserDtoOut create(UserDtoIn userDtoIn);

    void delete(Long userId);

    List<UserDtoOut> getAll(Set<Long> ids,
                            Pageable pageable);
}
