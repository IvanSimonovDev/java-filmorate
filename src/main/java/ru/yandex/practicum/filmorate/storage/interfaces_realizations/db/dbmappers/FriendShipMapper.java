package ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.dbmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.FriendShip;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class FriendShipMapper implements RowMapper<FriendShip> {
    public FriendShip mapRow(ResultSet rs, int currentRow) throws SQLException {
        FriendShip friendShip = new FriendShip();
        friendShip.setFriendId(rs.getLong("snd_user_id"));
        friendShip.setAcceptedByFriend(rs.getBoolean("accepted_by_second"));
        return friendShip;
    }
}
