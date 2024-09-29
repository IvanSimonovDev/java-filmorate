package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;
import java.util.Set;

public class FilmsServiceTests extends ParentServicesTestsClass {
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final FilmService filmService = new FilmService(filmStorage, new FilmsRatingComparator(), userStorage);
    private static Film film1;
    private static Film film2;
    private static Film film3;

    protected static Long filmId1;
    protected static Long filmId2;
    protected static Long filmId3;

    @Test
    public void shouldSetLikeWhenThereIsNotLikeFromUser() {
        filmService.setLike(filmId1, user1.getId());
        Assertions.assertEquals(film1.getLikes(), Set.of(user1.getId()));
    }

    @Test
    public void shouldNotSetLikeWhenThereIsAlreadyLikeFromUser() {
        Long filmFstId = filmId1;
        Long userFstId = user1.getId();
        filmService.setLike(filmFstId, userFstId);
        filmService.setLike(filmFstId, userFstId);
        Assertions.assertEquals(film1.getLikes(), Set.of(userFstId));
    }

    @Test
    public void deletionOfLikeShouldCauseNoEffectWhenLikeIsAbsent() {
        filmService.setLike(filmId1, user1.getId());
        filmService.deleteLike(filmId1, user2.getId());
        Assertions.assertEquals(film1.getLikes(), Set.of(user1.getId()));
    }

    @Test
    public void deletionOfLikeShouldWorkWhenLikePresents() {
        filmService.setLike(filmId1, user1.getId());
        filmService.deleteLike(filmId1, user1.getId());
        Assertions.assertEquals(film1.getLikes(), Set.of());
    }

    @Test
    public void shouldReturnMostPopularFilmsInCorrectOrder() {
        filmService.setLike(filmId2, user1.getId());
        filmService.setLike(filmId2, user2.getId());
        filmService.setLike(filmId2, user3.getId());
        filmService.setLike(filmId3, user1.getId());
        filmService.setLike(filmId3, user2.getId());
        filmService.setLike(filmId1, user1.getId());

        int maxFilms = 10;

        List<Film> currentResult = filmService.mostPopularFilms(maxFilms);
        List<Film> expectedResult = List.of(film2, film3, film1);
        Assertions.assertEquals(currentResult, expectedResult);
    }


    @BeforeEach
    public void initializeThreeFilmsAndIds() {
        film1 = createAndInitializeFilm();
        film2 = createAndInitializeFilm();
        film3 = createAndInitializeFilm();
        filmId1 = film1.getId();
        filmId2 = film2.getId();
        filmId3 = film3.getId();
    }

    @AfterEach
    public void deleteThreeFilms() {
        filmStorage.cleanStorage();
    }

    private Film createAndInitializeFilm() {
        Film film = new Film();
        filmStorage.createFilm(film);
        return film;
    }
}
