package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;
    private User userFst;
    private User userSnd;

    public User addFriend(Long userId, Long friendId) throws NotFoundException {
        setTwoUsers(userId, friendId);
        log.info("Making friendship for users with ids: {}, {}...", userId, friendId);
        userFst.getFriends().put(friendId, true);
        userSnd.getFriends().put(userId, true);
        return userSnd;
    }

    public User deleteFriend(Long userId, Long friendId) throws NotFoundException {
        setTwoUsers(userId, friendId);
        log.info("Removing friendship for users with ids: {}, {}...", userId, friendId);
        userFst.getFriends().remove(friendId);
        userSnd.getFriends().remove(userId);
        return userSnd;
    }

    public List<User> commonFriends(Long fstUserId, Long sndUserId) throws NotFoundException {
        setTwoUsers(fstUserId, sndUserId);
        log.info("Forming common friends list for users with ids: {}, {}...",
                fstUserId, sndUserId);
        Set<Long> userFstFriends = new HashSet<>(userFst.getFriends().keySet());
        Set<Long> userSndFriends = new HashSet<>(userSnd.getFriends().keySet());
        userFstFriends.retainAll(userSndFriends);
        return userFstFriends.stream().map(userStorage::returnUser).toList();
    }

    public List<User> userFriends(Long userId) throws NotFoundException {
        setTwoUsers(userId, userId);
        log.info("Forming friends list for user with id = {}...", userId);
        return userFst.getFriends().keySet().stream()
                .map(userStorage::returnUser)
                .toList();
    }

    private void setTwoUsers(Long userFstId, Long userSndId) throws NotFoundException {
        log.info("Started two users searching with ids: {}, {}", userFstId, userSndId);
        userFst = userStorage.returnUser(userFstId);
        userSnd = userStorage.returnUser(userSndId);
    }
}
