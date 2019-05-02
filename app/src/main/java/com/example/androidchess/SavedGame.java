package com.example.androidchess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SavedGame {
    private List<String> moves = new ArrayList<String>();
    private Date date;
    private String gameName;

    public SavedGame(String name, List<String> moves){
        setGameName(name);
        setMoves(moves);
        setDate();
    }

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }

    public Date getDate() {
        return date;
    }

    public void setDate() {
        this.date = date;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public String toString() {
        return gameName + " " + getDate();
    }
}
