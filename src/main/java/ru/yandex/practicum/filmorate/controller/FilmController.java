package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> filmsMemory = new HashMap<>();
    private Long currentId = (long) 0;

    private static final String NOT_FOUND_MESSAGE = "Film not found.";

    @GetMapping
    public Collection<Film> returnAllFilms() {
        return filmsMemory.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        logger.info("Started validation before film creation...");
        FilmValidator.validateFilm(film);
        logger.info("Validation passed.");
        Long newId = generateFilmId();
        film.setId(newId);
        filmsMemory.put(newId, film);
        logger.info("Film created and saved.");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) throws ValidationException {
        Long updatedFilmId = updatedFilm.getId();
        if (filmsMemory.containsKey(updatedFilmId)) {
            logger.info("Started validation before updating film with id = {}...", updatedFilmId);
            FilmValidator.validateFilm(updatedFilm);
            logger.info("Validation passed, started film updating...");
            Film oldFilm = filmsMemory.get(updatedFilmId);
            oldFilm.setName(updatedFilm.getName());
            oldFilm.setDescription(updatedFilm.getDescription());
            oldFilm.setReleaseDate(updatedFilm.getReleaseDate());
            oldFilm.setDuration(updatedFilm.getDuration());
            logger.info("Film with id = {} updated.", updatedFilmId);
            return oldFilm;
        } else {
            NotFoundException notFoundException = new NotFoundException(NOT_FOUND_MESSAGE);
            logger.error("Film with id = {} not found. Watch details below.", updatedFilmId);
            logger.error(NOT_FOUND_MESSAGE, notFoundException);
            throw notFoundException;
        }
    }

    private Long generateFilmId() {
        return ++currentId;
    }
}
