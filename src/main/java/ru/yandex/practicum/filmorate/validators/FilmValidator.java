package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;

public class FilmValidator {
    public static void validateFilm(Film film) throws ValidationException {
        boolean fieldsCorrect = validateFilmName(film.getName()) &&
                validateFilmDescription(film.getDescription()) &&
                validateFilmReleaseDate(film.getReleaseDate());
        if (!fieldsCorrect) {
            throw new ValidationException("Film didn't pass Validation");
        }
    }

    private static boolean validateFilmName(String name) {
        return !(UserValidator.isStringEmptyInJson(name));
    }

    private static boolean validateFilmDescription(String description) {
        int maxLength = 200;
        return description.length() <= maxLength;
    }

    private static boolean validateFilmReleaseDate(Instant releaseDate) {
        String minReleaseDateString = "1895-12-28T00:00:00Z";
        Instant minReleaseDate = Instant.parse(minReleaseDateString);
        return !releaseDate.isBefore(minReleaseDate);
    }


}
