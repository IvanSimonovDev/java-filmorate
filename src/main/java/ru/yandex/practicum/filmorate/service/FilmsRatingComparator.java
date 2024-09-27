package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

@Service
public class FilmsRatingComparator implements Comparator<Film> {
    public int compare(Film filmFst, Film filmSnd) {
        int filmFstLikesAmount = filmFst.getLikes().size();
        int filmSndLikesAmount = filmSnd.getLikes().size();
        if (filmFstLikesAmount == filmSndLikesAmount) {
            return 0;
        } else if (filmFstLikesAmount > filmSndLikesAmount) {
            return -1;
        } else {
            return 1;
        }
    }
}
