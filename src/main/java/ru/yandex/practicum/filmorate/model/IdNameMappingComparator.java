package ru.yandex.practicum.filmorate.model;

import java.util.Comparator;

public class IdNameMappingComparator implements Comparator<IdNameMapping> {
    public int compare(IdNameMapping mappingFst, IdNameMapping mappingSnd) {
        int mappingFstId = mappingFst.getId();
        int mappingSndId = mappingSnd.getId();
        return Integer.compare(mappingFstId, mappingSndId);
    }
}
