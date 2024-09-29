package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserServiceTests extends ParentServicesTestsClass {
    protected final UserService userService = new UserService(userStorage);

    @Test
    public void shouldCorrectlyAddToFriendsWhenThereAreNoFriends() {
        userService.addFriend(userId1, userId2);
        boolean condition1 = user1.getFriends().equals(Set.of(userId2));
        boolean condition2 = user2.getFriends().equals(Set.of(userId1));
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void shouldCorrectlyAddToFriendsWhenAlreadyHaveFriend() {
        userService.addFriend(userId1, userId2);
        userService.addFriend(userId1, userId3);
        boolean condition1 = user1.getFriends().equals(Set.of(userId2, userId3));
        boolean condition2 = user2.getFriends().equals(Set.of(userId1));
        boolean condition3 = user3.getFriends().equals(Set.of(userId1));
        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

    @Test
    public void friendsDeletionShouldCauseNoEffectWhenThereAreNoFriends() {
        userService.deleteFriend(userId1, userId2);
        boolean condition1 = user1.getFriends().equals(new HashSet<>());
        boolean condition2 = user2.getFriends().equals(new HashSet<>());
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void shouldDeleteFriendsWhenTheyExists() {
        userService.addFriend(userId1, userId2);
        userService.addFriend(userId1, userId3);
        userService.deleteFriend(userId1, userId2);
        userService.deleteFriend(userId1, userId3);
        boolean condition = user1.getFriends().equals(new HashSet<>());
        Assertions.assertTrue(condition);
    }

    @Test
    public void shouldNotFindCommonFriendsWhenThereAreNoCommonFriends() {
        List<User> commonFriendsUserFstUserSnd = userService.commonFriends(userId1, userId2);
        Assertions.assertTrue(commonFriendsUserFstUserSnd.isEmpty());
    }

    @Test
    public void shouldFindCommonFriendsWhenThereAreCommonFriends() {
        userService.addFriend(userId1, userId2);
        userService.addFriend(userId1, userId3);
        List<User> commonFriendsUserSndUserThd = userService.commonFriends(userId2, userId3);
        Assertions.assertEquals(Set.copyOf(commonFriendsUserSndUserThd), Set.of(user1));
    }

    @Test
    public void shouldReturnNoFriendsWhenThereAreNoFriends() {
        Assertions.assertTrue(userService.userFriends(userId1).isEmpty());
    }

    @Test
    public void shouldReturnAllFriendsWhenThereAreSome() {
        userService.addFriend(userId1, userId2);
        userService.addFriend(userId1, userId3);
        System.out.println(userService.userFriends(userId1));
        Set<User> userFstFriends = Set.copyOf(userService.userFriends(userId1));
        Assertions.assertEquals(userFstFriends, Set.of(user2, user3));
    }
}
