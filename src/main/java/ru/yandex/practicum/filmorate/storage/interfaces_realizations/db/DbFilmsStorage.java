package ru.yandex.practicum.filmorate.storage.interfaces_realizations.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.dbmappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Primary
@Repository
@RequiredArgsConstructor
public class DbFilmsStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private static final String NOT_FOUND_MESSAGE = "Film with id = {} not found.";
    private static final String DELETE_ALL_FILMS_SQL_QUERY = "DELETE FROM films;";

    private static final String RETURN_ALL_FILMS_SQL_QUERY = """
            SELECT id,
                   name,
                   description,
                   release_date,
                   duration
            FROM films;
            """;
    private static final String RETURN_ONE_FILM_SQL_QUERY = """
            SELECT id,
                   name,
                   description,
                   release_date,
                   duration
            FROM films
            WHERE id = ?;
            """;
    private static final String RETURN_FILM_RATING_SQL_QUERY = """
            SELECT r.name
            FROM films
            INNER JOIN ratings AS r ON films.rating_id = r.id
            WHERE films.id = ?;
            """;
    private static final String RETURN_RATING_ID_BY_NAME_SQL_QUERY = """
            SELECT id
            FROM ratings
            WHERE name = ?;
            """;

    private static final String RETURN_FILM_GENRES_SQL_QUERY = """
            SELECT genres.name
            FROM films
            INNER JOIN films_genres AS fg ON films.id = fg.film_id
            INNER JOIN genres ON fg.genre_id = genres.id
            WHERE films.id = ?;
            """;

    private static final String RETURN_GENRE_ID_BY_NAME_SQL_QUERY = """
            SELECT id
            FROM genres
            WHERE name = ?;
            """;

    private static final String INSERT_GENRE_OF_CERTAIN_FILM = """
            INSERT INTO films_genres (film_id, genre_id)
            VALUES (?, ?);
            """;

    private static final String DELETE_GENRES_OF_CERTAIN_FILM = """
                                                                DELETE FROM films_genres
                                                                WHERE film_id = ?;
                                                                """;

    private static final String RETURN_FILM_LIKES_SQL_QUERY = """
            SELECT likes.user_id
            FROM films
            INNER JOIN likes ON films.id = likes.film_id
            WHERE films.id = ?;
            """;

    private static final String INSERT_FILM_SQL_QUERY = """
            INSERT INTO films (id, name, description, release_date, duration, rating_id)
            VALUES (?, ?, ?, ?, ?, ?);
            """;

    private static final String UPDATE_FILMS_ROW_SQL_QUERY = """
            UPDATE films SET name = ?,
                             description = ?,
                             release_date = ?,
                             duration = ?,
                             rating_id = ?
            WHERE id = ?;
            """;

    private static final String GET_MAX_FILM_ID_SQL_QUERY = """
                                                            SELECT MAX(id)
                                                            FROM films;
                                                            """;

    public void cleanStorage() throws DataAccessException {
        jdbcTemplate.execute(DELETE_ALL_FILMS_SQL_QUERY);
    }

    public List<Film> returnAllFilms() throws DataAccessException {
        List<Film> films = jdbcTemplate.query(RETURN_ALL_FILMS_SQL_QUERY, filmMapper);
        return films.stream().peek(this::setFilmRatingGenresLikes).toList();
    }

    public Film returnFilm(Long id) throws NotFoundException, DataAccessException {
        Film film;
        try {
            film = jdbcTemplate.queryForObject(RETURN_ONE_FILM_SQL_QUERY, filmMapper, id);
            setFilmRatingGenresLikes(film);
        } catch (IncorrectResultSizeDataAccessException exc) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        return film;
    }

    public Film createFilm(Film film) throws DataAccessException {
        // Creating row in films
        film.setId(generateFilmId());
        int ratingId = getRatingIdByRatingName(film.getRating().toString());
        jdbcTemplate.update(INSERT_FILM_SQL_QUERY,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                ratingId);

        // Creating rows in films_genres

        insertFilmsGenresToDB(film);
        return film;
    }

    private long generateFilmId() throws DataAccessException {
        Long currentMaxId = jdbcTemplate.queryForObject(GET_MAX_FILM_ID_SQL_QUERY, Long.class);
        return ( currentMaxId == null ) ? 1 : (currentMaxId + 1);
    }
    private int getRatingIdByRatingName(String ratingName) throws DataAccessException {
        return jdbcTemplate.queryForObject(RETURN_RATING_ID_BY_NAME_SQL_QUERY, Integer.class, ratingName);
    }

    private Stream<Integer> getFilmsGenresIds(Film film) throws DataAccessException {
        Function<String, Integer> transformGenreNamesToIds =
                genreName -> jdbcTemplate.queryForObject(RETURN_GENRE_ID_BY_NAME_SQL_QUERY,
                        Integer.class,
                        genreName);

        return film.getGenres().stream()
                .map(Genre::toString)
                .map(transformGenreNamesToIds);
    }

    private void insertFilmsGenresToDB(Film film) {
        long filmId = film.getId();
        Consumer<Integer> insertMappingFilmGenre =
                (genreId) -> jdbcTemplate.update(INSERT_GENRE_OF_CERTAIN_FILM, filmId, genreId);
        getFilmsGenresIds(film).forEach(insertMappingFilmGenre);
    }

    public Film updateFilm(Film updatedFilm) throws NotFoundException {
        Film film;
        try {
            int ratingId = getRatingIdByRatingName(updatedFilm.getRating().toString());
            jdbcTemplate.update(UPDATE_FILMS_ROW_SQL_QUERY,
                    updatedFilm.getName(),
                    updatedFilm.getDescription(),
                    updatedFilm.getReleaseDate(),
                    updatedFilm.getDuration(),
                    ratingId,
                    updatedFilm.getId());

            jdbcTemplate.update(DELETE_GENRES_OF_CERTAIN_FILM, updatedFilm.getId());
            insertFilmsGenresToDB(updatedFilm);

            return returnFilm(updatedFilm.getId());
        } catch (DataIntegrityViolationException exc) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    private void setFilmRatingGenresLikes(Film film) {
        Long id = film.getId();
        Rating rating = Rating.valueOf(jdbcTemplate.queryForObject(RETURN_FILM_RATING_SQL_QUERY, String.class, id));
        film.setRating(rating);

        List<String> filmGenresList = jdbcTemplate.queryForList(RETURN_FILM_GENRES_SQL_QUERY, String.class, id);
        Set<Genre> filmGenresSet = Set.copyOf(filmGenresList.stream().map(Genre::valueOf).toList());
        film.setGenres(filmGenresSet);

        List<Long> filmsLikesList = jdbcTemplate.queryForList(RETURN_FILM_LIKES_SQL_QUERY, Long.class, id);
        Set<Long> filmLikesSet = Set.copyOf(filmsLikesList);
        film.setLikes(filmLikesSet);
    }
}
