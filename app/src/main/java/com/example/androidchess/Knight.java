package com.example.androidchess;


import android.graphics.drawable.Drawable;

/**
 *
 * @author John Sweeney
 * @author Denny Sabu
 *
 */

public class Knight extends Piece{
    /**
     * Kight constructor for making White and Black Knights
     * @param type String
     * @param space Space
     */
    public Knight(String type, Space space, Drawable x) {
        super(type, space, x);
    }

    /**
     * implementation of inherited abstract method for calculating moves
     */
    public void calculateMoves() {
        Space curSpace = getSpace();
        String [] tmpMoves;
        String [] basicMoves = new String[8];
        char column = curSpace.getName().charAt(0);
        int row = Character.getNumericValue(curSpace.getName().charAt(1));
        ++column;
        ++column;
        String upperRightLower = column + Integer.toString(row+1);
        basicMoves[0] = upperRightLower;
        String lowerRightUpper = column + Integer.toString(row-1);
        basicMoves[1] = lowerRightUpper;
        --column;
        String upperRightUpper = column + Integer.toString(row+2);
        basicMoves[2] = upperRightUpper;
        String lowerRightLower = column + Integer.toString(row-2);
        basicMoves[3] = lowerRightLower;
        --column;
        --column;
        String upperLeftUpper = column + Integer.toString(row+2);
        basicMoves[4] = upperLeftUpper;
        String lowerLeftLower = column + Integer.toString(row-2);
        basicMoves[5] = lowerLeftLower;
        --column;
        String upperLeftLower = column + Integer.toString(row+1);
        basicMoves[6] = upperLeftLower;
        String lowerLeftUpper = column + Integer.toString(row-1);
        basicMoves[7] = lowerLeftUpper;
        ++column;
        ++column;

        int tmpMovesSize = 0;
        for(int i = 0; i < basicMoves.length; i++) {
            char tmpColumn = basicMoves[i].charAt(0);
            int tmpRow = Integer.parseInt(basicMoves[i].substring(1));
            if(tmpColumn < 'a' || tmpRow < 1 || tmpColumn > 'h' || tmpRow > 8) {
                basicMoves[i] = "no";
            }
            else {
                tmpMovesSize++;
            }
        }

        tmpMoves = new String[tmpMovesSize];
        int index = 0;
        for(int i = 0; i < basicMoves.length; i++) {
            if(!(basicMoves[i].equals("no"))){
                tmpMoves[index++] = basicMoves[i];
            }
        }

        String [] moves = cleanMoves(tmpMoves);
        setMoves(moves);
    }

    /**
     * After calculating the basic moves they need to be clean of spaces that are occupied by pieces of the same color or blocking pieces
     * @param bmoves String[]
     * @return array of clean move set
     */
    public String [] cleanMoves(String [] bmoves) {
        for(int i = 0; i < bmoves.length; i++) {
            Space tmp = Board.fetchSpace(bmoves[i]);
            if(tmp.getPiece() != null) {
                if(tmp.getPiece().getType().charAt(0) == this.getType().charAt(0) && tmp.getPiece().getType().charAt(1) == 'K') {
                    bmoves[i] = "no";
                }
            }
        }

        int sizeOfFin = 0;
        for(int i = 0; i < bmoves.length; i++) {
            if(!(bmoves[i].equals("no")))
                sizeOfFin++;
        }

        String [] fin = new String[sizeOfFin];
        int index = 0;
        for(int i = 0; i < bmoves.length; i++) {
            if(!(bmoves[i].equals("no")))
                fin[index++] = bmoves[i];
        }

        return fin;
    }

    /**
     * Implementation of inherited abstract  method from piece for calculating the path to the king
     */
    public void calcPathToKing(Space kingSpot) {
        String [] path = new String[1];
        path[0] = kingSpot.getName();
        setPathToKing(path);
    }

}
