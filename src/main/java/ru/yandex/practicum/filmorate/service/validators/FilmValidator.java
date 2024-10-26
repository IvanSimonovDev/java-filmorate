package ru.yandex.practicum.filmorate.service.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreRatingDto;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Slf4j
public class FilmValidator {
    private static final String NOT_PASSED_MESSAGE = "Film didn't pass Validation";

    public static void validateFilm(Film film) throws ValidationException {
        boolean fieldsCorrect = validateFilmName(film.getName()) &&
                validateFilmDescription(film.getDescription()) &&
                validateFilmReleaseDate(film.getReleaseDate()) &&
                validateFilmDuration(film.getDuration()) &&
                validateFilmGenresIds(film.getGenres()) &&
                validateFilmMpa(film.getMpa());
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

    private static boolean validateFilmGenresIds(Set<GenreRatingDto> filmGenreIdsContainers) {
        List<Integer> validGenresIds = List.of(1, 2, 3, 4, 5, 6);
        List<Integer> filmGenreIds = null;
        if (filmGenreIdsContainers != null) {
            filmGenreIds = filmGenreIdsContainers.stream().map(GenreRatingDto::getId).toList();
        }
        return filmGenreIds == null || validGenresIds.containsAll(filmGenreIds) || filmGenreIds.isEmpty();
    }

    private static boolean validateFilmMpa(GenreRatingDto filmMpaContainer) {
        List<Integer> validRatingIds = List.of(1, 2, 3, 4, 5);
        return filmMpaContainer != null && validRatingIds.contains(filmMpaContainer.getId());
    }
}
