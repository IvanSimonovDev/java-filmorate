package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> filmsMemory = new HashMap<>();
    private Long currentId = (long) 0;

    private static final String NOT_FOUND_MESSAGE = "Film not found.";

    @GetMapping
    public Collection<Film> returnAllFilms() {
        return new ArrayList<>(filmsMemory.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.info("Started validation before film creation...");
        FilmValidator.validateFilm(film);
        log.info("Validation passed.");
        Long newId = generateFilmId();
        film.setId(newId);
        filmsMemory.put(newId, film);
        log.info("Film created and saved.");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) throws ValidationException {
        Long updatedFilmId = updatedFilm.getId();
        if (filmsMemory.containsKey(updatedFilmId)) {
            log.info("Started validation before updating film with id = {}...", updatedFilmId);
            FilmValidator.validateFilm(updatedFilm);
            log.info("Validation passed, started film updating...");
            Film oldFilm = filmsMemory.get(updatedFilmId);
            oldFilm.setName(updatedFilm.getName());
            oldFilm.setDescription(updatedFilm.getDescription());
            oldFilm.setReleaseDate(updatedFilm.getReleaseDate());
            oldFilm.setDuration(updatedFilm.getDuration());
            log.info("Film with id = {} updated.", updatedFilmId);
            return oldFilm;
        } else {
            NotFoundException notFoundException = new NotFoundException(NOT_FOUND_MESSAGE);
            log.error("Film with id = {} not found. Watch details below.", updatedFilmId);
            log.error(NOT_FOUND_MESSAGE, notFoundException);
            throw notFoundException;
        }
    }

    private Long generateFilmId() {
        return ++currentId;
    }
}
