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
    private Long id;
    private String name;
    private String description;
    private String releaseDate;
    // Duration in Seconds
    private Integer duration;
}
