package ru.practicum.ewm.service.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.service.user.dto.UserDtoIn;
import ru.practicum.ewm.service.user.dto.UserDtoOut;

import java.util.List;

public interface IUserService {
    UserDtoOut create(UserDtoIn userDtoIn);

    void delete(Long userId);

    List<UserDtoOut> getAll(List<Long> ids,
                            Pageable pageable);
}
