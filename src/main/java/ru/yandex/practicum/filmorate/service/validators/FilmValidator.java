package ru.yandex.practicum.filmorate.service.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;

@Slf4j
public class FilmValidator {
    private static final String NOT_PASSED_MESSAGE = "Film didn't pass Validation";

    public static void validateFilm(Film film) throws ValidationException {
        boolean fieldsCorrect = validateFilmName(film.getName()) &&
                validateFilmDescription(film.getDescription()) &&
                validateFilmReleaseDate(film.getReleaseDate()) &&
                validateFilmDuration(film.getDuration());
        if (!fieldsCorrect) {
            ValidationException validationException = new ValidationException(NOT_PASSED_MESSAGE);
            log.error(NOT_PASSED_MESSAGE, validationException);
            throw validationException;
        }
    }

    private static boolean validateFilmName(String name) {
        return !(UserValidator.isStringEmptyInJson(name));
    }

    private static boolean validateFilmDescription(String description) {
        int maxLength = 200;
        return description.length() <= maxLength;
    }

    private static boolean validateFilmReleaseDate(String releaseDateString) {
        String minReleaseDateString = "1895-12-28T00:00:00Z";
        Instant minReleaseDate = Instant.parse(minReleaseDateString);

        String additionForCorrectFormat = "T00:00:00Z";
        Instant releaseDate = Instant.parse(releaseDateString + additionForCorrectFormat);

        return !releaseDate.isBefore(minReleaseDate);
    }

    private static boolean validateFilmDuration(Integer duration) {
        return duration > 0;
    }
}
