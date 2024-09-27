package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.service.validators.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.validators.UserValidator;

public class UserValidatorTest {
    private User user;

    @BeforeEach
    public void initializeFilm() {
        user = new User((long) 1, "user.user@yandex.ru", "user", "Dan", "1999-09-09", null);
    }

    @Test
    public void validationPassedWhenUserCorrect() {
        Assertions.assertDoesNotThrow(
                () -> UserValidator.validateUser(user)
        );
    }


    @Test
    public void validationNotPassedWhenEmailIsEmpty() {
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validateUser(user)
        );
    }

    @Test
    public void validationNotPassedWhenEmailContainsOnlyWhitespaces() {
        user.setEmail("      ");
        Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validateUser(user)
        );
    }

    @Test
    public void validationNotPassedWhenEmailDoesNotContainAt() {
        user.setEmail("user.useryandex.ru");
        Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validateUser(user)
        );
    }

    @Test
    public void validationNotPassedWhenEmailIsNull() {
        user.setEmail(null);
        Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validateUser(user)
        );
    }

    @Test
    public void validationNotPassedWhenLoginIsEmpty() {
        user.setLogin("");
        Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validateUser(user)
        );
    }

    @Test
    public void validationNotPassedWhenLoginContainsOnlyWhitespaces() {
        user.setLogin("      ");
        Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validateUser(user)
        );
    }

    @Test
    public void validationNotPassedWhenEmailDoesNotContainWhitespace() {
        user.setLogin("user user");
        Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validateUser(user)
        );
    }

    @Test
    public void validationNotPassedWhenLoginIsNull() {
        user.setLogin(null);
        Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validateUser(user)
        );
    }

    @Test
    public void validationNotPassedWhenBirthDayIsAfterNow() {
        user.setBirthday("2050-01-01");
        Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validateUser(user)
        );
    }

    @Test
    public void validationPassedWhenBirthDayIsBeforeNow() {
        user.setBirthday("2000-01-01");
        Assertions.assertDoesNotThrow(
                () -> UserValidator.validateUser(user)
        );
    }
}
