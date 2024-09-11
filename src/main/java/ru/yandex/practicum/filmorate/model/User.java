package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
    Long id;
    String email;
    String login;
    String name;
    Instant birthday;
}
