package com.example.androidchess;


import java.util.*;

/**
 *
 * @author John Sweeney
 * @author Denny Sabu
 *
 */

public class Rook extends Piece {
    /**
     * Rook class that serves for both white and black Rooks
     */
    private String [] partitionMoves;

    /*
     * Constructor for creating rooks
     */
    public Rook(String type, Space space) {
        super(type, space);
    }

    /**
     * implementation of inherited abstract method for calculating moves
     */
    public void calculateMoves() {
        Space curSpace = getSpace();
        String [] tmpMoves;
        tmpMoves = new String[19];
        char column = curSpace.getName().charAt(0);
        int row = Character.getNumericValue(curSpace.getName().charAt(1));
        int index = 0;
        tmpMoves[index++] = "||";
        for(int i = row + 1; i <= 8; i++) {
            String movesUp = column + Integer.toString(i);
            tmpMoves[index++] = movesUp;
        }
        tmpMoves[index++] = "||";

        for(int i = row - 1; i >= 1; i--) {
            String movesDown = column + Integer.toString(i);
            tmpMoves[index++] = movesDown;
        }
        tmpMoves[index++] = "||";

        char tmp = ++column;
        --column;
        for(char i = tmp; i <= 'h'; i++) {
            String movesRight = i + Integer.toString(row);
            tmpMoves[index++] = movesRight;
        }
        tmpMoves[index++] = "||";

        char tmp2 = --column;
        ++column;
        for(char i = tmp2; i >= 'a'; i--) {
            String movesLeft = i + Integer.toString(row);
            tmpMoves[index++] = movesLeft;
        }

        tmpMoves[index++] = "||";

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
