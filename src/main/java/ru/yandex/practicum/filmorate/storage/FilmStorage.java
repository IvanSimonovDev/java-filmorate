package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void cleanStorage();

    List<Film> returnAllFilms();

    Film returnFilm(Long id) throws NotFoundException;

    Film createFilm(Film film);

    Film updateFilm(Film updatedFilm) throws NotFoundException;
}
