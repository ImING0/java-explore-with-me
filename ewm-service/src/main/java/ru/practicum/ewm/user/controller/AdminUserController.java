package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserDtoIn;
import ru.practicum.ewm.user.dto.UserDtoOut;
import ru.practicum.ewm.user.service.admin.IUserAdminService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final IUserAdminService userService;

    @PostMapping
    public ResponseEntity<UserDtoOut> createUser(@RequestBody @Valid UserDtoIn userDtoIn) {
        log.info("Creating user {}", userDtoIn);

        UserDtoOut userDtoOut = userService.create(userDtoIn);

        return new ResponseEntity<>(userDtoOut, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDtoOut>> getAllUsers(
            @RequestParam(name = "ids", required = false) Set<Long> ids,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Getting users with ids {} from {} to {}", ids, from, from + size);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<UserDtoOut> users = userService.getAll(ids, pageable);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Deleting user with id {}", userId);
        userService.delete(userId);
    }
}
