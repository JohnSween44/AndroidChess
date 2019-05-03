package com.example.androidchess;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SavedGame implements Serializable {
    private List<String> moves = new ArrayList<String>();
    private String date;
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

    public String getDate() {
        return date;
    }

    public void setDate() {
        DateFormat dateFormat = new SimpleDateFormat("HH:MM dd/MM");
        Date curDate = new Date();
        date = dateFormat.format(curDate);
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
