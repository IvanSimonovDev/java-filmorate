package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.validators.UserValidator;
import ru.yandex.practicum.filmorate.service.validators.ValidationException;
import ru.yandex.practicum.filmorate.storage.NotFoundException;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserStorage userstorage;
    private final UserService userService;

    @GetMapping
    public Collection<User> returnAllUsers() {
        return userstorage.returnAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        log.info("Started validation before user creation...");
        UserValidator.validateUser(user);
        log.info("Validation passed.");
        return userstorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) throws ValidationException, NotFoundException {
        log.info("Started validation before updating User...");
        UserValidator.validateUser(updatedUser);
        log.info("Validation passed, started user updating...");
        return userstorage.updateUser(updatedUser);
    }

    @GetMapping("/{id}")
    public User returnUser(@PathVariable Long id) throws NotFoundException {
        return userstorage.returnUser(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> returnUserFriends(@PathVariable Long id) throws NotFoundException {
        return userService.userFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable Long id, @PathVariable Long friendId) throws NotFoundException {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFromFriends(@PathVariable Long id, @PathVariable Long friendId) throws NotFoundException {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> returnCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.commonFriends(id, otherId);
    }

}
