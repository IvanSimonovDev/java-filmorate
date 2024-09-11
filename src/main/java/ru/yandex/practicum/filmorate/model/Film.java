package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Film {
    Long id;
    String name;
    String description;
    String releaseDate;
    // Duration in Seconds
    Integer duration;
}
