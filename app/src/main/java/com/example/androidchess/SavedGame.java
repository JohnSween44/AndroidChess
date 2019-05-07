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
    private Date realDate;
    private String gameName;
    private String winner;

    public SavedGame(String name, List<String> moves, String winner){
        setWinner(winner);
        setGameName(name);
        setMoves(moves);
        setDate();
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
        moves.add(getWinner());
    }

    public String getDate() {
        return date;
    }

    public Date getRealDate() {
        return realDate;
    }

    public void setRealDate(Date realDate) {
        this.realDate = realDate;
    }

    public void setDate() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM");
        Date curDate = new Date();
        date = dateFormat.format(curDate);
        realDate = curDate;
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
