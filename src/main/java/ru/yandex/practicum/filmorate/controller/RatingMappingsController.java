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
@RequestMapping("/mpa")
public class RatingMappingsController {

    private final DbFilmsGenresAndRatingsStorage dbFilmsGenresAndRatingsStorage;

    @GetMapping
    public Collection<IdNameMapping> returnAllRatingsMappings() {
        return dbFilmsGenresAndRatingsStorage.returnAllRatingsMappings();
    }

    @GetMapping("/{id}")
    public IdNameMapping returnRatingMapping(@PathVariable Long id) throws NotFoundException {
        return dbFilmsGenresAndRatingsStorage.returnRatingMappingById(id);
    }

}
