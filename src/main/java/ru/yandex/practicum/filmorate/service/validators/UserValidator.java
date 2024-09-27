package ru.yandex.practicum.filmorate.service.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;

@Slf4j
public class UserValidator {
    private static final String notPassedMessage = "User didn't pass Validation";

    public static void validateUser(User user) throws ValidationException {
        boolean fieldsCorrect = validateUserEmail(user.getEmail()) &&
                validateUserLogin(user.getLogin()) &&
                validateUserBirthday(user.getBirthday());
        if (!fieldsCorrect) {
            ValidationException validationException = new ValidationException(notPassedMessage);
            log.error(notPassedMessage, validationException);
            throw validationException;
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

    private static boolean validateUserBirthday(String birthdayString) {
        String additionForCorrectFormat = "T00:00:00Z";
        Instant birthday = Instant.parse(birthdayString + additionForCorrectFormat);
        return birthday.isBefore(Instant.now());
    }

    public static boolean isStringEmptyInJson(String string) {
        return string == null || string.isBlank();
    }


}
