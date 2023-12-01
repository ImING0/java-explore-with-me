package ru.practicum.ewm.user.service.admin.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.error.DataConflictException;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.user.dto.UserDtoIn;
import ru.practicum.ewm.user.dto.UserDtoOut;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.QUser;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.admin.IUserAdminService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAdminService implements IUserAdminService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDtoOut create(UserDtoIn userDtoIn) {
        User userToSave = UserMapper.toUser(userDtoIn);
        User saved;
        try {
            saved = userRepository.saveAndFlush(userToSave);
        } catch (DataIntegrityViolationException ex) {
            throw new DataConflictException(ex.getMessage(), ex.getCause());
        }
        return UserMapper.toUserDtoOut(saved);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User userToDelete = getUserOrThrow(userId);
        userRepository.delete(userToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDtoOut> getAll(Set<Long> ids,
                                   Pageable pageable) {

        QUser user = QUser.user;
        BooleanExpression predicate = user.isNotNull();
        if (ids != null && !ids.isEmpty()) {
            predicate = predicate.and(user.id.in(ids));
        }

        return userRepository.findAll(predicate, pageable)
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
