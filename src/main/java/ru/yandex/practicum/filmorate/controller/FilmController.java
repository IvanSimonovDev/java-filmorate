package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> filmsMemory = new HashMap<>();
    private Long currentId = (long) 0;

    @GetMapping
    public Collection<Film> returnAllFilms() {
        return filmsMemory.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        FilmValidator.validateFilm(film);
        filmsMemory.put(generateFilmId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) throws ValidationException {
        Long updatedFilmId = updatedFilm.getId();
        if (filmsMemory.containsKey(updatedFilmId)) {
            FilmValidator.validateFilm(updatedFilm);
            Film oldFilm = filmsMemory.get(updatedFilmId);
            oldFilm.setName(updatedFilm.getName());
            oldFilm.setDescription(updatedFilm.getDescription());
            oldFilm.setReleaseDate(updatedFilm.getReleaseDate());
            oldFilm.setDuration(updatedFilm.getDuration());
            return oldFilm;
        } else {
            return createFilm(updatedFilm);
        }
    }

    private Long generateFilmId() {
        return ++currentId;
    }
}
