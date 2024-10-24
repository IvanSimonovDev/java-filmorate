package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmsLikesComparator filmsLikesComparator;
    private final UserStorage userStorage;

    public Film setLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.returnFilm(filmId);
        userStorage.returnUser(userId);
        log.info("Adding like to film(id = {}) from user(id = {})...", filmId, userId);
        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.returnFilm(filmId);
        userStorage.returnUser(userId);
        log.info("Removing like from film(id = {}) by user(id = {})...", filmId, userId);
        film.getLikes().remove(userId);
        return film;
    }

    public List<Film> mostPopularFilms(int maxFilms) {
        log.info("Forming top films list...");
        List<Film> result = filmStorage.returnAllFilms();
        int storageSize = result.size();
        int maxFilmsFromList = storageSize < maxFilms ? storageSize : maxFilms;
        result.sort(filmsLikesComparator);
        return result.subList(0, maxFilmsFromList);
    }
}
