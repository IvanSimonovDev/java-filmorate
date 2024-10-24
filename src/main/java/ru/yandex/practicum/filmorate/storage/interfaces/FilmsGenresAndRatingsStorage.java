package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.GenreMapping;
import ru.yandex.practicum.filmorate.model.RatingMapping;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;

import java.util.List;

public interface FilmsGenresAndRatingsStorage {
    List<GenreMapping> returnAllGenresMappings();
    List<RatingMapping> returnAllRatingsMappings();
    GenreMapping returnGenreMappingById(Long id) throws NotFoundException;
    RatingMapping returnRatingMappingById(Long id) throws NotFoundException;

}
