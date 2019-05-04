package com.example.androidchess;

import java.util.Comparator;

public class SortGamesByName implements Comparator<SavedGame> {


    @Override
    public int compare(SavedGame o1, SavedGame o2) {
        int ret = o1.getGameName().compareTo(o2.getGameName());
        return ret;
    }
}
