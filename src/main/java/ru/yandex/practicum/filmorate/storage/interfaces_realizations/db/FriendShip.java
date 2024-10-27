package ru.yandex.practicum.filmorate.storage.interfaces_realizations.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendShip {
    private Long friendId;
    private boolean acceptedByFriend;
}
