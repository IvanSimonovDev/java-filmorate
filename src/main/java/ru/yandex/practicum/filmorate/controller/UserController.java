package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> usersMemory = new HashMap<>();
    private Long currentId = (long) 0;

    private static final String NOT_FOUND_MESSAGE = "User not found.";

    @GetMapping
    public Collection<User> returnAllUsers() {
        return new ArrayList<>(usersMemory.values());
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        log.info("Started validation before user creation...");
        UserValidator.validateUser(user);
        log.info("Validation passed.");
        setUserName(user, user.getName(), user.getLogin());
        Long newId = generateUserId();
        user.setId(newId);
        usersMemory.put(newId, user);
        log.info("User created and saved.");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) throws ValidationException {
        Long updatedUserId = updatedUser.getId();
        if (usersMemory.containsKey(updatedUserId)) {
            log.info("Started validation before updating User with id = {}...", updatedUserId);
            UserValidator.validateUser(updatedUser);
            log.info("Validation passed, started user updating...");
            User oldUser = usersMemory.get(updatedUserId);
            oldUser.setEmail(updatedUser.getEmail());
            oldUser.setLogin(updatedUser.getLogin());
            setUserName(oldUser, updatedUser.getName(), updatedUser.getLogin());
            oldUser.setBirthday(updatedUser.getBirthday());
            log.info("User with id = {} updated.", updatedUserId);
            return oldUser;
        } else {
            NotFoundException notFoundException = new NotFoundException(NOT_FOUND_MESSAGE);
            log.error("Film with id = {} not found. Watch details below.", updatedUserId);
            log.error(NOT_FOUND_MESSAGE, notFoundException);
            throw notFoundException;
        }
    }

    private void setUserName(User user, String name1, String name2) {
        if (UserValidator.isStringEmptyInJson(name1)) {
            user.setName(name2);
        } else {
            user.setName(name1);
        }
    }

    private Long generateUserId() {
        return ++currentId;
    }
}
