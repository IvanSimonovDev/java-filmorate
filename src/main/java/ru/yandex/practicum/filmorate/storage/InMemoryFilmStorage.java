package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private static final String NOT_FOUND_MESSAGE = "Film not found.";
    private final Map<Long, Film> filmsMemory = new HashMap<>();
    private Long currentId = (long) 0;

    public void cleanStorage() {
        log.info("Films storage cleaning started...");
        filmsMemory.clear();
        log.info("Films storage cleaning completed.");
    }

    public List<Film> returnAllFilms() {
        return new ArrayList<>(filmsMemory.values());
    }

    public Film returnFilm(Long id) throws NotFoundException {
        log.info("Film searching started...");
        Film result = filmsMemory.get(id);
        if (result == null) {
            NotFoundException notFoundException = new NotFoundException(NOT_FOUND_MESSAGE);
            log.info(NOT_FOUND_MESSAGE, notFoundException);
            throw notFoundException;
        }
        log.info("Film found.");
        return result;
    }

    public Film createFilm(Film film) {
        log.info("Started film creation...");
        Long newId = generateFilmId();
        film.setId(newId);
        film.setLikes(new HashSet<>());
        filmsMemory.put(newId, film);
        log.info("Film created and saved.");
        return film;
    }

    public Film updateFilm(Film updatedFilm) throws NotFoundException {
        Long updatedFilmId = updatedFilm.getId();
        if (filmsMemory.containsKey(updatedFilmId)) {
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
