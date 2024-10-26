package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.GenreRatingDto;

import java.util.List;

public interface FilmsGenresAndRatingsStorage {
    List<GenreRatingDto> returnAllGenresMappings();

    List<GenreRatingDto> returnAllRatingsMappings();

    GenreRatingDto returnGenreMappingById(Long id) throws NotFoundException;

    GenreRatingDto returnRatingMappingById(Long id) throws NotFoundException;

}
