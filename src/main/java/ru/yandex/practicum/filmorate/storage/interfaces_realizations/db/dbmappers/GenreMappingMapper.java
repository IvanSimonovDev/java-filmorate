package ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.dbmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.GenreRatingDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class GenreMappingMapper implements RowMapper<GenreRatingDto> {
    public GenreRatingDto mapRow(ResultSet rs, int currentRow) throws SQLException {
        GenreRatingDto idNameMapping = new GenreRatingDto();
        idNameMapping.setId(rs.getInt("id"));
        idNameMapping.setName(rs.getString("name"));
        return idNameMapping;
    }
}