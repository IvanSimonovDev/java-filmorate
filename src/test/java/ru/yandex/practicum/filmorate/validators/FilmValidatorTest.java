package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.validators.FilmValidator;
import ru.yandex.practicum.filmorate.service.validators.ValidationException;

import java.time.Duration;
import java.time.Instant;

public class FilmValidatorTest {
    private Film film;
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final Instant MIN_RELEASE_DATE = Instant.parse("1895-12-28T00:00:00Z");

    @BeforeEach
    public void initializeFilm() {
        film = new Film((long) 1, "Film1", "Description1", "2022-02-02", 3600, null);
    }

    @Test
    public void validationPassedWhenFilmCorrect() {
        Assertions.assertDoesNotThrow(
                () -> FilmValidator.validateFilm(film)
        );
    }

    @Test
    public void validationNotPassedWhenNameIsEmpty() {
        film.setName("");
        Assertions.assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(film)
        );
    }

    @Test
    public void validationNotPassedWhenNameContainsOnlyWhitespaces() {
        film.setName("      ");
        Assertions.assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(film)
        );
    }

    @Test
    public void validationNotPassedWhenNameIsNull() {
        film.setName(null);
        Assertions.assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(film)
        );
    }


    @Test
    public void validationNotPassedWhenLengthOfDescriptionIsMoreThanMax() {
        String description = (new StringBuilder()).repeat("a", MAX_DESCRIPTION_LENGTH + 1).toString();
        film.setDescription(description);
        Assertions.assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(film)
        );
    }

    @Test
    public void validationPassedWhenLengthOfDescriptionIsMax() {
        String description = (new StringBuilder()).repeat("a", MAX_DESCRIPTION_LENGTH).toString();
        film.setDescription(description);
        Assertions.assertDoesNotThrow(
                () -> FilmValidator.validateFilm(film)
        );
    }

    @Test
    public void validationNotPassedWhenReleaseDateIsBeforeMin() {
        String releaseDateString = MIN_RELEASE_DATE.minus(Duration.ofDays(1)).toString().split("T")[0];
        film.setReleaseDate(releaseDateString);
        Assertions.assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(film)
        );
    }

    @Test
    public void validationPassedWhenReleaseDateIsMin() {
        String releaseDateString = MIN_RELEASE_DATE.toString().split("T")[0];
        film.setReleaseDate(releaseDateString);
        Assertions.assertDoesNotThrow(
                () -> FilmValidator.validateFilm(film)
        );
    }

    @Test
    public void validationPassedWhenReleaseDateIsAfterMin() {
        String releaseDateString = MIN_RELEASE_DATE.plus(Duration.ofDays(1)).toString().split("T")[0];
        film.setReleaseDate(releaseDateString);
        Assertions.assertDoesNotThrow(
                () -> FilmValidator.validateFilm(film)
        );
    }

    @Test
    public void validationNotPassedWhenDurationIsNegative() {
        int duration = -20;
        film.setDuration(duration);
        Assertions.assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(film)
        );
    }

    @Test
    public void validationNotPassedWhenDurationIsZero() {
        int duration = 0;
        film.setDuration(duration);
        Assertions.assertThrows(ValidationException.class,
                () -> FilmValidator.validateFilm(film)
        );
    }

    @Test
    public void validationPassedWhenDurationIsMoreThanZero() {
        int duration = 320;
        film.setDuration(duration);
        Assertions.assertDoesNotThrow(
                () -> FilmValidator.validateFilm(film)
        );
    }
}
