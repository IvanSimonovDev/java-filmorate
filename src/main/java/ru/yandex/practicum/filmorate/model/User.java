package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private String birthday;
    private Set<Long> friends;
}
