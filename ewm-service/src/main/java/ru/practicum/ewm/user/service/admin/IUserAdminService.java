package ru.practicum.ewm.user.service.admin;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.user.dto.UserDtoIn;
import ru.practicum.ewm.user.dto.UserDtoOut;

import java.util.List;

public interface IUserAdminService {
    UserDtoOut create(UserDtoIn userDtoIn);

    void delete(Long userId);

    List<UserDtoOut> getAll(List<Long> ids,
                            Pageable pageable);
}
