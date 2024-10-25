package ru.yandex.practicum.filmorate.storage.interfaces_realizations.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdNameMapping;
import ru.yandex.practicum.filmorate.model.IdNameMappingComparator;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.dbmappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.dbmappers.GenreMappingMapper;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;

@Primary
@Repository
@RequiredArgsConstructor
public class DbFilmsStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final GenreMappingMapper genreMappingMapper;
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

    private static final String RETURN_FILM_RATING_ID_SQL_QUERY = """
            SELECT rating_id
            FROM films
            WHERE id = ?;
            """;

    private static final String RETURN_RATING_NAME_BY_ID = """
            SELECT name
            FROM ratings
            WHERE id = ?;
            """;
    private static final String RETURN_FILM_GENRES_IDS_AND_NAMES_SQL_QUERY = """
            SELECT g.id,
                   g.name
            FROM films_genres AS fg
            INNER JOIN genres AS g ON fg.genre_id = g.id
            WHERE film_id = ?;
            """;

    private static final String INSERT_GENRE_ID_OF_CERTAIN_FILM = """
            INSERT INTO films_genres (film_id, genre_id)
            VALUES (?, ?);
            """;

    private static final String DELETE_GENRES_OF_CERTAIN_FILM = """
            DELETE FROM films_genres
            WHERE film_id = ?;
            """;
    private static final String RETURN_FILM_LIKES_SQL_QUERY = """
            SELECT user_id
            FROM likes
            WHERE film_id = ?;
            """;

    private static final String INSERT_FILM_LIKE_SQL_QUERY = """
            INSERT INTO likes (user_id, film_id)
            VALUES (?, ?);
            """;

    private static final String DELETE_LIKES_OF_CERTAIN_FILM = """
            DELETE FROM likes
            WHERE film_id = ?;
            """;

    private static final String INSERT_FILM_WITHOUT_GENRES_SQL_QUERY = """
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
        return films.stream().map(this::setFilmRatingGenresLikes).toList();
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
        long ratingId = film.getMpa().getId();
        jdbcTemplate.update(INSERT_FILM_WITHOUT_GENRES_SQL_QUERY,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                ratingId);

        // Creating rows in films_genres
        insertFilmsGenresIdsToDB(film);
        return returnFilm(film.getId());
    }

    private long generateFilmId() throws DataAccessException {
        Long currentMaxId = jdbcTemplate.queryForObject(GET_MAX_FILM_ID_SQL_QUERY, Long.class);
        return (currentMaxId == null) ? 1 : (currentMaxId + 1);
    }

    private void insertFilmsGenresIdsToDB(Film film) {
        if (film.getGenres() != null) {
            List<Integer> filmGenresIds = film.getGenres().stream().map(IdNameMapping::getId).toList();
            long filmId = film.getId();
            Consumer<Integer> insertMappingFilmGenre =
                    (genreId) -> jdbcTemplate.update(INSERT_GENRE_ID_OF_CERTAIN_FILM, filmId, genreId);
            filmGenresIds.forEach(insertMappingFilmGenre);
        }
    }

    public Film updateFilm(Film updatedFilm) throws NotFoundException {
        Film film;
        try {
            Long updatedFilmId = updatedFilm.getId();
            //updating table films
            int ratingId = updatedFilm.getMpa().getId();
            jdbcTemplate.update(UPDATE_FILMS_ROW_SQL_QUERY,
                    updatedFilm.getName(),
                    updatedFilm.getDescription(),
                    updatedFilm.getReleaseDate(),
                    updatedFilm.getDuration(),
                    ratingId,
                    updatedFilmId);

            //updating table films_genres
            jdbcTemplate.update(DELETE_GENRES_OF_CERTAIN_FILM, updatedFilmId);
            insertFilmsGenresIdsToDB(updatedFilm);

            //updating table likes
            Set<Long> filmLikes = updatedFilm.getLikes();
            if (!(filmLikes == null || filmLikes.isEmpty())) {
                jdbcTemplate.update(DELETE_LIKES_OF_CERTAIN_FILM, updatedFilmId);
                Function<Long, Integer> insertLikeOfFilm = (userId) ->
                        jdbcTemplate.update(INSERT_FILM_LIKE_SQL_QUERY, userId, updatedFilmId);
                filmLikes.stream().map(insertLikeOfFilm).toList();
            }

            return returnFilm(updatedFilmId);
        } catch (DataIntegrityViolationException exc) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    private Film setFilmRatingGenresLikes(Film film) {
        Long filmId = film.getId();

        //Setting rating
        Integer ratingId = jdbcTemplate.queryForObject(RETURN_FILM_RATING_ID_SQL_QUERY, Integer.class, filmId);
        String ratingName = jdbcTemplate.queryForObject(RETURN_RATING_NAME_BY_ID, String.class, ratingId);
        film.setMpa(new IdNameMapping(ratingId, ratingName));

        //Setting genres
        List<IdNameMapping> filmGenresMappingsList = jdbcTemplate.query(RETURN_FILM_GENRES_IDS_AND_NAMES_SQL_QUERY,
                genreMappingMapper, filmId);
        TreeSet<IdNameMapping> filmGenresMappingsSet = new TreeSet<>(new IdNameMappingComparator());
        filmGenresMappingsSet.addAll(filmGenresMappingsList);
        film.setGenres(filmGenresMappingsSet);

        // Setting likes
        List<Long> filmsLikesList = jdbcTemplate.queryForList(RETURN_FILM_LIKES_SQL_QUERY, Long.class, filmId);
        Set<Long> filmLikesSet = Set.copyOf(filmsLikesList);
        film.setLikes(filmLikesSet);

        return film;
    }
}
