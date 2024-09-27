package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public class ParentServicesTestsClass {
    protected static User user1;
    protected static User user2;
    protected static User user3;

    protected static Long userId1;
    protected static Long userId2;
    protected static Long userId3;
    protected final UserStorage userStorage = new InMemoryUserStorage();

    @BeforeEach
    public void initializeThreeUsersAndIds() {
        user1 = createAndInitializeUser();
        user2 = createAndInitializeUser();
        user3 = createAndInitializeUser();
        userId1 = user1.getId();
        userId2 = user2.getId();
        userId3 = user3.getId();
    }

    private User createAndInitializeUser() {
        User user = new User();
        userStorage.createUser(user);
        return user;
    }

    @AfterEach
    public void deleteThreeUsers() {
        userStorage.cleanStorage();
    }
}
