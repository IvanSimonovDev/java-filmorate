package ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.dbmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMapping;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class RatingMappingMapper implements RowMapper<RatingMapping> {
    public RatingMapping mapRow(ResultSet rs, int currentRow) throws SQLException {
        RatingMapping genreMapping = new RatingMapping();
        genreMapping.setId(rs.getInt("id"));
        genreMapping.setRating(Rating.valueOf(rs.getString("name")));
        return genreMapping;
    }
}