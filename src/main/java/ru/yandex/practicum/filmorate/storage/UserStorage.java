package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void cleanStorage();

    List<User> returnAllUsers();

    User returnUser(Long id) throws NotFoundException;

    User createUser(User user);

    User updateUser(User updatedUser) throws NotFoundException;
}
