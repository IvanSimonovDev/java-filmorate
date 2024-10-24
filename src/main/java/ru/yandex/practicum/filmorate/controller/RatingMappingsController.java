package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreMapping;
import ru.yandex.practicum.filmorate.model.RatingMapping;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.validators.FilmValidator;
import ru.yandex.practicum.filmorate.service.validators.ValidationException;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.DbFilmsGenresAndRatingsStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class RatingMappingsController {

    private final DbFilmsGenresAndRatingsStorage dbFilmsGenresAndRatingsStorage;

    @GetMapping
    public Collection<RatingMapping> returnAllRatingsMappings() {
        return dbFilmsGenresAndRatingsStorage.returnAllRatingsMappings();
    }

    @GetMapping("/{id}")
    public RatingMapping returnRatingMapping(@PathVariable Long id) throws NotFoundException {
        return dbFilmsGenresAndRatingsStorage.returnRatingMappingById(id);
    }

}
