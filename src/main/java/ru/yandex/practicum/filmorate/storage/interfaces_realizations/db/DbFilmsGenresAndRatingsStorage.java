package ru.yandex.practicum.filmorate.storage.interfaces_realizations.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.GenreRatingDto;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsGenresAndRatingsStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.dbmappers.GenreMappingMapper;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.dbmappers.RatingMappingMapper;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class DbFilmsGenresAndRatingsStorage implements FilmsGenresAndRatingsStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMappingMapper genreMappingMapper;
    private final RatingMappingMapper ratingMappingMapper;

    private static final String GENRE_NOT_FOUND_MESSAGE = "Genre with id = {} not found.";
    private static final String RATING_NOT_FOUND_MESSAGE = "Rating with id = {} not found.";

    private static final String RETURN_ALL_GENREMAPPINGS_SQL_QUERY = """
            SELECT id, name
            FROM genres;
            """;

    private static final String RETURN_ALL_RATINGMAPPINGS_SQL_QUERY = """
            SELECT id, name
            FROM ratings;
            """;

    private static final String RETURN_GENREMAPPING_BY_ID_SQL_QUERY = """
            SELECT id, name
            FROM genres
            WHERE id = ?;
            """;

    private static final String RETURN_RATINGMAPPING_BY_ID_SQL_QUERY = """
            SELECT id, name
            FROM ratings
            WHERE id = ?;
            """;


    public List<GenreRatingDto> returnAllGenresMappings() {
        return jdbcTemplate.query(RETURN_ALL_GENREMAPPINGS_SQL_QUERY, genreMappingMapper);
    }

    public List<GenreRatingDto> returnAllRatingsMappings() {
        return jdbcTemplate.query(RETURN_ALL_RATINGMAPPINGS_SQL_QUERY, ratingMappingMapper);
    }

    public GenreRatingDto returnGenreMappingById(Long id) throws NotFoundException {
        GenreRatingDto genreMapping;

        try {
            genreMapping = jdbcTemplate.queryForObject(RETURN_GENREMAPPING_BY_ID_SQL_QUERY, genreMappingMapper, id);
        } catch (IncorrectResultSizeDataAccessException exc) {
            throw new NotFoundException(GENRE_NOT_FOUND_MESSAGE);
        }
        return genreMapping;
    }

    public GenreRatingDto returnRatingMappingById(Long id) throws NotFoundException {
        GenreRatingDto ratingMapping;

        try {
            ratingMapping = jdbcTemplate.queryForObject(RETURN_RATINGMAPPING_BY_ID_SQL_QUERY, ratingMappingMapper, id);
        } catch (IncorrectResultSizeDataAccessException exc) {
            throw new NotFoundException(RATING_NOT_FOUND_MESSAGE);
        }
        return ratingMapping;
    }
}
