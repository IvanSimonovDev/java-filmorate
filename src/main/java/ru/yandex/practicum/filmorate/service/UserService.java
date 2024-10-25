package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.NotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        if (!isUserFstAddedToFriendsOfUserSnd(friendId, userId)) {
            log.info("Making friendship for users with ids: {}, {}...", userId, friendId);
            if (isUserFstAddedToFriendsOfUserSnd(userId, friendId)) {
                makeOneDirectionFriendShip(userSnd, userId, true);
                userStorage.updateUser(userSnd);
                makeOneDirectionFriendShip(userFst, friendId, true);
            } else {
                makeOneDirectionFriendShip(userFst, friendId, false);
            }
            userStorage.updateUser(userFst);
        }
        return userStorage.returnUser(userId);
    }

    public User deleteFriend(Long userId, Long friendId) throws NotFoundException {
        setTwoUsers(userId, friendId);
        if (isUserFstAddedToFriendsOfUserSnd(friendId, userId)) {
            log.info("Removing friendship for users with ids: {}, {}...", userId, friendId);
            Map<Long, Boolean> friendsOfUserFst = userFst.getFriends();
            friendsOfUserFst.remove(friendId);
            userFst.setFriends(friendsOfUserFst);
            if (isUserFstAddedToFriendsOfUserSnd(userId, friendId)) {
                makeOneDirectionFriendShip(userSnd, userId, false);
                userStorage.updateUser(userSnd);
            }
            userStorage.updateUser(userFst);
        }
        return userStorage.returnUser(userId);
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

    private boolean isUserFstAddedToFriendsOfUserSnd(Long userFstId, Long userSndId) {
        User userFst = userStorage.returnUser(userFstId);
        User userSnd = userStorage.returnUser(userSndId);
        return userSnd.getFriends().containsKey(userFstId);
    }

    private void makeOneDirectionFriendShip(User user, Long friendId, boolean status) {
        Map<Long, Boolean> friendsOfUser = user.getFriends();
        friendsOfUser.put(friendId, status);
        user.setFriends(friendsOfUser);
    }
}
