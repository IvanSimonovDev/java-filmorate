package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.DbFilmsGenresAndRatingsStorage;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.DbFilmsStorage;
import ru.yandex.practicum.filmorate.storage.interfaces_realizations.db.DbUsersStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Set;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final DbFilmsStorage filmStorage;
    private final DbUsersStorage userStorage;
    private final DbFilmsGenresAndRatingsStorage dbFilmsGenresAndRatingsStorage;
    private Film film1;
    private Film film2;
    private User user1;
    private User user2;

    @Test
    void contextLoads() {}

    @Test
    public void canCreateFilmsInDBAndReceiveAllFilms() {
        List<Film> films = filmStorage.returnAllFilms();
        boolean condition1 = films.isEmpty();
        createTwoFilmsInDB();
        boolean condition2 = filmStorage.returnAllFilms().getFirst().getName().equals("Film1");
        boolean condition3 = filmStorage.returnAllFilms().get(1).getName().equals("Film2");
        boolean condition4 = filmStorage.returnAllFilms().size() == 2;
        Assertions.assertTrue(condition1 && condition2 && condition3 && condition4);
    }

    @Test
    public void canDeleteAllFilms() {
        createTwoFilmsInDB();
        boolean condition1 = filmStorage.returnAllFilms().size() == 2;
        filmStorage.cleanStorage();
        boolean condition2 = filmStorage.returnAllFilms().isEmpty();
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void canReturnFilmFromDbById() {
        createTwoFilmsInDB();
        Film filmReturnedFromAll = filmStorage.returnAllFilms().getFirst();
        long id = filmReturnedFromAll.getId();
        Film filmReturnedFromSearchById = filmStorage.returnFilm(id);

        Assertions.assertEquals(filmReturnedFromAll, filmReturnedFromSearchById);
    }

    @Test
    public void shouldThrowExceptionWhenFilmNotFoundAfterRequestForFilm() {
        Assertions.assertThrows(NotFoundException.class,
                () -> filmStorage.returnFilm((long) 0));
    }

    @Test
    public void shouldUpdateFilmWhenDataIsCorrect() {
        createTwoFilmsInDB();
        Long id = filmStorage.returnAllFilms().getFirst().getId();
        film1 = filmStorage.returnFilm(id);
        String newName = "New Name";
        film1.setName(newName);
        filmStorage.updateFilm(film1);
        Film updatedFilm = filmStorage.returnFilm(id);
        Assertions.assertEquals(updatedFilm.getName(), newName);
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingFilmNotExists() {
        createTwoFilmsInDB();
        film1 = filmStorage.returnAllFilms().getFirst();
        film1.setId((long) 1000);
        Assertions.assertThrows(NotFoundException.class,
                () -> filmStorage.updateFilm(film1));
    }

    @Test
    public void canCreateUsersInDbAndGetAllOfThem() {
        List<User> users = userStorage.returnAllUsers();
        boolean condition1 = users.isEmpty();
        createTwoUsersInDB();
        boolean condition2 = userStorage.returnAllUsers().getFirst().getName().equals("Kate");
        boolean condition3 = userStorage.returnAllUsers().get(1).getName().equals("Nick");
        boolean condition4 = userStorage.returnAllUsers().size() == 2;
        Assertions.assertTrue(condition1 && condition2 && condition3 && condition4);
    }

    @Test
    public void canDeleteAllUsersFromDb() {
        createTwoUsersInDB();
        userStorage.cleanStorage();
        Assertions.assertTrue(userStorage.returnAllUsers().isEmpty());
    }

    @Test
    public void canReturnUserFromDbById() {
        createTwoUsersInDB();
        User userReturnedFromAll = userStorage.returnAllUsers().getFirst();
        long id = userReturnedFromAll.getId();
        User userReturnedFromSearchById = userStorage.returnUser(id);

        Assertions.assertEquals(userReturnedFromAll, userReturnedFromSearchById);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFoundAfterRequestForUser() {
        Assertions.assertThrows(NotFoundException.class,
                () -> userStorage.returnUser((long) 1));
    }

    @Test
    public void shouldUpdateUserWhenDataIsCorrect() {
        createTwoUsersInDB();
        Long id = userStorage.returnAllUsers().getFirst().getId();
        user1 = userStorage.returnUser(id);
        String newName = "New Name";
        user1.setName(newName);
        userStorage.updateUser(user1);
        User updatedUser = userStorage.returnUser(id);
        Assertions.assertEquals(updatedUser.getName(), newName);
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingUserNotExists() {
        createTwoUsersInDB();
        user1 = userStorage.returnAllUsers().getFirst();
        user1.setId((long) 1000);
        Assertions.assertThrows(NotFoundException.class,
                () -> userStorage.updateUser(user1));
    }

    @Test
    public void dbShouldReturnAllGenreMappings() {
        List<IdNameMapping> allGenreMappings = dbFilmsGenresAndRatingsStorage.returnAllGenresMappings();
        Assertions.assertEquals(allGenreMappings.size(), 6);
    }

    @Test
    public void dbShouldReturnGenreMappingById() {
        IdNameMapping returnedGenreMapping = dbFilmsGenresAndRatingsStorage.returnGenreMappingById((long) 2);
        Assertions.assertEquals(returnedGenreMapping.getName(), "Драма");
    }

    @Test
    public void dbShouldReturnAllRatingMappings() {
        List<IdNameMapping> allRatingMappings = dbFilmsGenresAndRatingsStorage.returnAllRatingsMappings();
        Assertions.assertEquals(allRatingMappings.size(), 5);
    }

    @Test
    public void dbShouldReturnRatingMappingById() {
        IdNameMapping returnedRatingMapping = dbFilmsGenresAndRatingsStorage.returnRatingMappingById((long) 2);
        Assertions.assertEquals(returnedRatingMapping.getName(), "PG");
    }

    @BeforeEach
    public void createAndInitializeTwoFilmObjects() {
        film1 = new Film();
        film1.setName("Film1");
        film1.setDescription("Description_1");
        film1.setReleaseDate("2001-01-01");
        film1.setDuration(60);
        film1.setLikes(Set.of());
        film1.setGenres(Set.of(new IdNameMapping(1, "Комедия"),
                new IdNameMapping(2, "Драма")));
        film1.setMpa(new IdNameMapping(1, "G"));

        film2 = new Film();
        film2.setName("Film2");
        film2.setDescription("Description_2");
        film2.setReleaseDate("2002-01-01");
        film2.setDuration(90);
        film2.setLikes(Set.of());
        film2.setGenres(Set.of(new IdNameMapping(1, "Комедия")));
        film2.setMpa(new IdNameMapping(3, "PG-13"));
    }

    @BeforeEach
    public void createAndInitializeTwoUsersObjects() {
        user1 = new User();
        user1.setEmail("lupa@mail.ru");
        user1.setLogin("lupa15");
        user1.setName("Kate");
        user1.setBirthday("1980-02-03");
        user1.setFriends(Map.of());

        user2 = new User();
        user2.setEmail("pupa@mail.ru");
        user2.setLogin("pupa21");
        user2.setName("Nick");
        user2.setBirthday("1981-02-16");
        user2.setFriends(Map.of());
    }

    @AfterEach
    public void clearStorages() {
        filmStorage.cleanStorage();
        userStorage.cleanStorage();
    }

    private void createTwoFilmsInDB() {
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
    }

    private void createTwoUsersInDB() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
    }
}
