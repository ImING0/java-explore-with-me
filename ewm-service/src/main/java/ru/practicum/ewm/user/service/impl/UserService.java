package ru.practicum.ewm.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.user.dto.UserDtoIn;
import ru.practicum.ewm.user.dto.UserDtoOut;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.IUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public UserDtoOut create(UserDtoIn userDtoIn) {
        User userToSave = UserMapper.toUser(userDtoIn);
        return UserMapper.toUserDtoOut(userRepository.save(userToSave));
    }

    @Override
    public void delete(Long userId) {
        User userToDelete = getUserOrThrow(userId);
        userRepository.delete(userToDelete);
    }

    @Override
    public List<UserDtoOut> getAll(List<Long> ids,
                                   Pageable pageable) {
        return userRepository.findAllByIdIn(ids, pageable)
                .stream()
                .map(UserMapper::toUserDtoOut)
                .collect(Collectors.toList());
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with id %d not found", userId)));
    }
}
