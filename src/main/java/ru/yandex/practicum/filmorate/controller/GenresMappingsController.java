package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.IdNameMapping;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.DbFilmsGenresAndRatingsStorage;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenresMappingsController {

    private final DbFilmsGenresAndRatingsStorage dbFilmsGenresAndRatingsStorage;

    @GetMapping
    public Collection<IdNameMapping> returnAllGenresMappings() {
        return dbFilmsGenresAndRatingsStorage.returnAllGenresMappings();
    }

    @GetMapping("/{id}")
    public IdNameMapping returnGenreMapping(@PathVariable Long id) throws NotFoundException {
        return dbFilmsGenresAndRatingsStorage.returnGenreMappingById(id);
    }

}
