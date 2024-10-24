package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.dao.DataAccessException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void cleanStorage() throws DataAccessException;

    List<Film> returnAllFilms() throws DataAccessException;

    Film returnFilm(Long id) throws NotFoundException, DataAccessException;

    Film createFilm(Film film) throws DataAccessException;

    Film updateFilm(Film updatedFilm) throws NotFoundException;
}
