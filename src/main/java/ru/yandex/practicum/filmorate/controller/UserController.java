package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> usersMemory = new HashMap<>();
    private Long currentId = (long) 0;

    @GetMapping
    public Collection<User> returnAllUsers() {
        return usersMemory.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        UserValidator.validateUser(user);
        usersMemory.put(generateUserId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) throws ValidationException {
        Long updatedUserId = updatedUser.getId();
        if (usersMemory.containsKey(updatedUserId)) {
            UserValidator.validateUser(updatedUser);
            User oldUser = usersMemory.get(updatedUserId);
            oldUser.setEmail(updatedUser.getEmail());
            oldUser.setLogin(updatedUser.getLogin());
            setUserName(oldUser, updatedUser.getName(), updatedUser.getLogin());
            oldUser.setBirthday(updatedUser.getBirthday());
            return oldUser;
        } else {
            return createUser(updatedUser);
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
