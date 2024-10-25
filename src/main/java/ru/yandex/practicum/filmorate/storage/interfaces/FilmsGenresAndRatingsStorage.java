package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.IdNameMapping;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;

import java.util.List;

public interface FilmsGenresAndRatingsStorage {
    List<IdNameMapping> returnAllGenresMappings();
    List<IdNameMapping> returnAllRatingsMappings();
    IdNameMapping returnGenreMappingById(Long id) throws NotFoundException;
    IdNameMapping returnRatingMappingById(Long id) throws NotFoundException;

}
