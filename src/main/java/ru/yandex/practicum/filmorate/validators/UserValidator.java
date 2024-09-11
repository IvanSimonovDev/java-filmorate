package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;

public class UserValidator {
    public static void validateUser(User user) throws ValidationException {
        boolean fieldsCorrect = validateUserEmail(user.getEmail()) &&
                validateUserLogin(user.getLogin()) &&
                validateUserBirthday(user.getBirthday());
        if (!fieldsCorrect) {
            throw new ValidationException("User didn't pass Validation");
        }
    }

    private static boolean validateUserEmail(String email) {
        boolean isEmailNotCorrect = isStringEmptyInJson(email) || !email.contains("@");
        return !(isEmailNotCorrect);
    }

    private static boolean validateUserLogin(String login) {
        boolean isLoginNotCorrect = isStringEmptyInJson(login) || login.contains(" ");
        return !isLoginNotCorrect;
    }

    private static boolean validateUserBirthday(Instant birthDay) {
        return birthDay.isBefore(Instant.now());
    }

    public static boolean isStringEmptyInJson(String string) {
        return string == null || string.isBlank();
    }


}
