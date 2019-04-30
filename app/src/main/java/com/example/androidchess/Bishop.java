package com.example.androidchess;


import android.graphics.drawable.Drawable;

import java.util.*;

/**
 *
 * @author John Sweeney
 * @author Denny Sabu
 *
 */

public class Bishop extends Piece{
    private String [] partitionMoves;

    /**
     * Bishop class for making white and black bishops
     * @param type String
     * @param space Space
     */
    public Bishop(String type, Space space, Drawable x) {
        super(type, space, x);
    }

    public Bishop(Bishop bishop){
        super(bishop);
    }
    /**
     * implementation of inherited abstract method for calculating moves
     */
    public void calculateMoves() {
        Space curSpace = getSpace();
        String [] tmpMoves;
        String [] basicMoves = new String[20];
        char column = curSpace.getName().charAt(0);
        int row = Character.getNumericValue(curSpace.getName().charAt(1));
        char tmpcolumn = ++column;
        --column;
        int index = 0;
        basicMoves[index++] = "||";
        for(int i = row + 1; i <= 8 && tmpcolumn <= 'h'; i++) {
            String upRight = tmpcolumn + Integer.toString(i);
            basicMoves[index++] = upRight;
            tmpcolumn++;
        }
        basicMoves[index++] = "||";
        tmpcolumn = --column;
        ++column;
        for(int i = row + 1; i <= 8 && tmpcolumn >= 'a'; i++) {
            String upLeft = tmpcolumn + Integer.toString(i);
            basicMoves[index++] = upLeft;
            tmpcolumn--;
        }
        basicMoves[index++] = "||";
        tmpcolumn = ++column;
        --column;
        for(int i = row - 1; i >= 1 && tmpcolumn <= 'h'; i--) {
            String downRight = tmpcolumn + Integer.toString(i);
            basicMoves[index++] = downRight;
            tmpcolumn++;
        }
        basicMoves[index++] = "||";
        tmpcolumn = --column;
        ++column;
        for(int i = row - 1; i >= 1 && tmpcolumn >= 'a'; i--) {
            String downLeft = tmpcolumn + Integer.toString(i);
            basicMoves[index++] = downLeft;
            tmpcolumn--;
        }
        basicMoves[index++] = "||";
        int y = 0;
        int size = 0;
        while(basicMoves[y] != null) {
            size++;
            y++;
        }

        tmpMoves = new String[size];

        for(int i = 0; i < tmpMoves.length; i++) {
            tmpMoves[i] = basicMoves[i];
        }

        String [] moves = cleanMoves(tmpMoves);
        setMoves(moves);
    }

    /**
     * After calculating the basic moves they need to be clean of spaces that are occupied by pieces of the same color or blocking pieces
     * @param bmoves String []
     * @return array of clean move set
     */
    public String [] cleanMoves(String [] bmoves) {
        for(int i = 0; i < bmoves.length; i++) {
            if(bmoves[i].equals("||"))
                continue;
            Space tmp = Board.fetchSpace(bmoves[i]);
            Piece tmpPiece = tmp.getPiece();
            if(tmpPiece != null) {
                if(tmpPiece.getType().charAt(0) != this.getType().charAt(0) && tmpPiece.getType().charAt(1) == 'K'){
                    for(int k = i + 1; !(bmoves[k].equals("||")); k++) {
                        i = k;
                    }
                }
                else if(tmpPiece.getType().charAt(0) == this.getType().charAt(0) && tmpPiece.getType().charAt(1) == 'K'){
                    bmoves[i] = "no";
                    for(int k = i + 1; !(bmoves[k].equals("||")); k++) {
                        bmoves[k] = "no";
                        i = k;
                    }
                }
                else {
                    for(int k = i + 1; !(bmoves[k].equals("||")); k++) {
                        bmoves[k] = "no";
                        i = k;
                    }
                }
            }
        }

        partitionMoves = bmoves;

        int sizeOfFin = 0;
        for(int i = 0; i < bmoves.length; i++) {
            if(!(bmoves[i].equals("no")) && !(bmoves[i].equals("||"))) {
                sizeOfFin++;
            }
        }

        int index = 0;
        String [] fin = new String[sizeOfFin];
        for(int i = 0; i < bmoves.length; i++) {
            if(!(bmoves[i].equals("no")) && !(bmoves[i].equals("||"))) {
                fin[index++] = bmoves[i];
            }
        }

        return fin;
    }

    /**
     * Implementation of inherited abstract  method from piece for calculating the path to the king
     */
    public void calcPathToKing(Space kingSpot) {
        List<String> path = new ArrayList<String>();
        List<String> range = new ArrayList<String>();
        int i = 0;
        while(!(kingSpot.getName().equals(partitionMoves[i]))) {
            i++;
        }
        for(int k = i; !(partitionMoves[k].equals("||")); k--){
            path.add(partitionMoves[k]);
        }
        String [] tmp = new String[path.size()];
        tmp = path.toArray(tmp);
        setPathToKing(tmp);

        int j = i;
        while(!("||".equals(partitionMoves[j]))) {
            j++;
        }

        for(int k = j - 1; !(partitionMoves[k].equals("||")); k--){
            range.add(partitionMoves[k]);
        }
        String [] tmp2 = new String[path.size()];
        tmp2 = path.toArray(tmp2);
        setRangeOfCheck(tmp2);
    }
}
