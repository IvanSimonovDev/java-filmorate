package ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.dbmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.GenreMapping;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class GenreMappingMapper implements RowMapper<GenreMapping> {
    public GenreMapping mapRow(ResultSet rs, int currentRow) throws SQLException {
        GenreMapping genreMapping = new GenreMapping();
        genreMapping.setId(rs.getInt("id"));
        genreMapping.setGenre(Genre.valueOf(rs.getString("name")));
        return genreMapping;
    }
}
