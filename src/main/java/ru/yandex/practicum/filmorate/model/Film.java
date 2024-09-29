package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Film {
    private Long id;
    private String name;
    private String description;
    private String releaseDate;
    // Duration in Seconds
    private Integer duration;
    // Set contains ids of users who liked film
    private Set<Long> likes;
}
