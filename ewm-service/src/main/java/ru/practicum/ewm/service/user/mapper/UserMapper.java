package ru.practicum.ewm.service.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.service.user.dto.UserDtoIn;
import ru.practicum.ewm.service.user.dto.UserDtoOut;
import ru.practicum.ewm.service.user.model.User;

@UtilityClass
public class UserMapper {
    /**
     * Преобразовать UserDtoIn в User
     *
     * @param userDtoIn dto для создания пользователя
     * @return пользователь
     */
    public User toUser(UserDtoIn userDtoIn) {
        return User.builder()
                .name(userDtoIn.getName())
                .email(userDtoIn.getEmail())
                .build();
    }

    public UserDtoOut toUserDtoOut(User user) {
        return UserDtoOut.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
