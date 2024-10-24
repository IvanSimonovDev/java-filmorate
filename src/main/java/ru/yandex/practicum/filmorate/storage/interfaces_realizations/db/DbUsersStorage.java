package ru.yandex.practicum.filmorate.storage.interfaces_realizations.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.dbmappers.FriendShipMapper;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.dbmappers.UserMapper;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Primary
@Repository
@RequiredArgsConstructor
public class DbUsersStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    private final FriendShipMapper friendShipMapper;
    private static final String NOT_FOUND_MESSAGE = "User with id = {} not found.";
    private static final String DELETE_ALL_USERS_SQL_QUERY = "DELETE FROM users;";

    private static final String RETURN_ALL_USERS_SQL_QUERY = """
            SELECT id,
                   name,
                   birthday
            FROM users;
            """;
    private static final String RETURN_ONE_USER_SQL_QUERY = """
            SELECT id,
                   name,
                   birthday
            FROM users
            WHERE id = ?;
            """;
    private static final String RETURN_USER_EMAIL_SQL_QUERY = """
            SELECT e.email
            FROM users AS u
            INNER JOIN emails AS e ON u.id = e.user_id
            WHERE u.id = ?;
            """;
    private static final String RETURN_USER_LOGIN_SQL_QUERY = """
            SELECT l.login
            FROM users AS u
            INNER JOIN logins AS l ON u.id = l.user_id
            WHERE u.id = ?;
            """;

    private static final String RETURN_USER_FRIENDSHIPS_SQL_QUERY = """
            SELECT f.snd_user_id,
                   f.accepted_by_second
            FROM users AS u
            INNER JOIN friendships AS f ON u.id = f.fst_user_id
            WHERE u.id = ?;
            """;

    private static final String INSERT_USER_BASIC_INFO_SQL_QUERY = """
            INSERT INTO users (id, name, birthday)
            VALUES (?, ?, ?);
            """;

    private static final String INSERT_USER_EMAIL_SQL_QUERY = """
            INSERT INTO emails (user_id, email)
            VALUES (?, ?);
            """;

    private static final String INSERT_USER_LOGIN_SQL_QUERY = """
            INSERT INTO logins (user_id, login)
            VALUES (?, ?);
            """;

    private static final String GET_MAX_USER_ID_SQL_QUERY = """
            SELECT MAX(id)
            FROM users;
            """;

    private static final String UPDATE_USER_BASIC_INFO_SQL_QUERY = """
            UPDATE users SET name = ?,
                             birthday = ?
            WHERE id = ?;
            """;

    private static final String UPDATE_USER_EMAIL_SQL_QUERY = """
            UPDATE emails SET email = ?
            WHERE user_id = ?;
            """;

    private static final String UPDATE_USER_LOGIN_SQL_QUERY = """
            UPDATE logins SET login = ?
            WHERE user_id = ?;
            """;


    public void cleanStorage() throws DataAccessException {
        jdbcTemplate.execute(DELETE_ALL_USERS_SQL_QUERY);
    }

    public List<User> returnAllUsers() throws DataAccessException {
        List<User> users = jdbcTemplate.query(RETURN_ALL_USERS_SQL_QUERY, userMapper);
        return users.stream().peek(this::setUserEmailLoginFriends).toList();
    }

    public User returnUser(Long id) throws NotFoundException, DataAccessException {
        User user;
        try {
            user = jdbcTemplate.queryForObject(RETURN_ONE_USER_SQL_QUERY, userMapper, id);
            setUserEmailLoginFriends(user);
        } catch (IncorrectResultSizeDataAccessException exc) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        return user;
    }

    public User createUser(User user) throws DataAccessException {
        // Creating row in users
        user.setId(generateUserId());
        jdbcTemplate.update(INSERT_USER_BASIC_INFO_SQL_QUERY,
                user.getId(),
                user.getName(),
                user.getBirthday());

        // Creating rows in emails and logins

        insertUserEmailLoginToDB(user);
        return user;
    }

    private long generateUserId() throws DataAccessException {
        Long currentMaxId = jdbcTemplate.queryForObject(GET_MAX_USER_ID_SQL_QUERY, Long.class);
        return (currentMaxId == null) ? 1 : (currentMaxId + 1);
    }


    private void insertUserEmailLoginToDB(User user) {
        jdbcTemplate.update(INSERT_USER_EMAIL_SQL_QUERY, user.getId(), user.getEmail());
        jdbcTemplate.update(INSERT_USER_LOGIN_SQL_QUERY, user.getId(), user.getLogin());
    }

    public User updateUser(User updatedUser) throws NotFoundException {
        User user;
        try {
            jdbcTemplate.update(UPDATE_USER_BASIC_INFO_SQL_QUERY,
                    updatedUser.getName(),
                    updatedUser.getBirthday(),
                    updatedUser.getId());

            jdbcTemplate.update(UPDATE_USER_EMAIL_SQL_QUERY,
                    updatedUser.getEmail(),
                    updatedUser.getId());

            jdbcTemplate.update(UPDATE_USER_LOGIN_SQL_QUERY,
                    updatedUser.getLogin(),
                    updatedUser.getId());


            return returnUser(updatedUser.getId());
        } catch (DataIntegrityViolationException exc) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    private void setUserEmailLoginFriends(User user) {
        Long id = user.getId();
        String email = jdbcTemplate.queryForObject(RETURN_USER_EMAIL_SQL_QUERY, String.class, id);
        user.setEmail(email);

        String login = jdbcTemplate.queryForObject(RETURN_USER_LOGIN_SQL_QUERY, String.class, id);
        user.setLogin(login);

        List<FriendShip> userFriendshipsList = jdbcTemplate.query(RETURN_USER_FRIENDSHIPS_SQL_QUERY,
                friendShipMapper,
                id);
        Map<Long, Boolean> userFriendshipsMap = new HashMap<>();
        Consumer<FriendShip> friendShipProcessing = friendship ->
                userFriendshipsMap.put(friendship.getFriendId(), friendship.isAcceptedByFriend());
        userFriendshipsList.forEach(friendShipProcessing);
        user.setFriends(userFriendshipsMap);
    }


}
