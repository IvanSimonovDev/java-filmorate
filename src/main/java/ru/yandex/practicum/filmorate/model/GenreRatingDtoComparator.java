package ru.yandex.practicum.filmorate.model;

import java.util.Comparator;

public class GenreRatingDtoComparator implements Comparator<GenreRatingDto> {
    public int compare(GenreRatingDto mappingFst, GenreRatingDto mappingSnd) {
        int mappingFstId = mappingFst.getId();
        int mappingSndId = mappingSnd.getId();
        return Integer.compare(mappingFstId, mappingSndId);
    }
}
