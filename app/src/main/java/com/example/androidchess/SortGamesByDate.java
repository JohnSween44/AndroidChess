package com.example.androidchess;

import java.util.Comparator;

public class SortGamesByDate implements Comparator<SavedGame> {

    @Override
    public int compare(SavedGame o1, SavedGame o2) {
        int ret = o1.getRealDate().compareTo(o2.getRealDate());
        return ret;
    }
}
