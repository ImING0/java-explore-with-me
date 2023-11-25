package ru.practicum.ewm.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.dto.UserDtoIn;
import ru.practicum.ewm.user.dto.UserDtoOut;
import ru.practicum.ewm.user.dto.UserShortDtoOut;
import ru.practicum.ewm.user.model.User;

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

    /**
     * Преобразовать User в UserDtoOut
     *
     * @param user пользователь
     * @return dto пользователя
     */
    public UserDtoOut toUserDtoOut(User user) {
        return UserDtoOut.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Преобразовать User в UserShortDtoOut
     *
     * @param user пользователь
     * @return dto пользователя
     */
    public UserShortDtoOut toUserShortDtoOut(User user) {
        return UserShortDtoOut.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
