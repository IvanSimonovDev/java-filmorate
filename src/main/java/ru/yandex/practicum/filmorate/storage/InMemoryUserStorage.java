package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.validators.UserValidator;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> usersMemory = new HashMap<>();
    private Long currentId = (long) 0;
    private static final String NOT_FOUND_MESSAGE = "User not found.";

    public void cleanStorage() {
        log.info("User storage cleaning started...");
        usersMemory.clear();
        log.info("User storage cleaning ended...");
    }

    public List<User> returnAllUsers() {
        return new ArrayList<>(usersMemory.values());
    }

    public User returnUser(Long id) throws NotFoundException {
        log.info("User searching started...");
        User result = usersMemory.get(id);
        if (result == null) {
            NotFoundException notFoundException = new NotFoundException(NOT_FOUND_MESSAGE);
            log.info(NOT_FOUND_MESSAGE, notFoundException);
            throw notFoundException;
        }
        log.info("User found.");
        return result;
    }

    public User createUser(User user) {
        setUserName(user, user.getName(), user.getLogin());
        Long newId = generateUserId();
        user.setId(newId);
        user.setFriends(new HashSet<>());
        usersMemory.put(newId, user);
        log.info("User created and saved.");
        return user;
    }

    public User updateUser(User updatedUser) throws NotFoundException {
        Long updatedUserId = updatedUser.getId();
        if (usersMemory.containsKey(updatedUserId)) {
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
