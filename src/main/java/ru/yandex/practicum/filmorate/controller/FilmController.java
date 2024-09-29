package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.validators.FilmValidator;
import ru.yandex.practicum.filmorate.service.validators.ValidationException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.NotFoundException;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> returnAllFilms() {
        return filmStorage.returnAllFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.info("Started validation before film creation...");
        FilmValidator.validateFilm(film);
        log.info("Validation passed.");
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) throws ValidationException, NotFoundException {
        log.info("Started validation before updating film...");
        FilmValidator.validateFilm(updatedFilm);
        log.info("Validation passed, started film updating...");
        return filmStorage.updateFilm(updatedFilm);
    }

    @GetMapping("/{id}")
    public Film returnFilm(@PathVariable Long id) throws NotFoundException {
        return filmStorage.returnFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable("id") Long filmId, @PathVariable Long userId) throws NotFoundException {
        return filmService.setLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeFromFilm(@PathVariable("id") Long filmId, @PathVariable Long userId)
            throws NotFoundException {
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> returnMostPopularFilms(@RequestParam Optional<Integer> count) {
        Integer defaultCount = 10;
        return filmService.mostPopularFilms(count.orElse(defaultCount));
    }
}
