package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Map;

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
    // True - friendship is accepted, false - otherwise
    private Map<Long, Boolean> friends;
}
