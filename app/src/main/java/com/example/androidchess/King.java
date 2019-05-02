package com.example.androidchess;

import android.graphics.drawable.Drawable;

import java.util.*;

/**
 *
 * @author John Sweeney
 * @author Denny Sabu
 *
 */

public class King extends Piece{
    /**
     * King class used to make both white kings and Black Kings
     * fields
     * 	-castle:boolean to see check if the king is eligible
     * 	-castleLanding: the spot the king would go to if they castle
     */
    private boolean castle = false;
    private String castleLanding;

    /**
     * constructor for making kings
     * @param type (String)
     * @param space (Space)
     */
    public King(String type, Space space, Drawable x) {
        super(type, space, x);
    }

    public King(King king){
        super(king);
    }

    /**
     * @return castle boolean
     */
    public boolean isCastle() {
        return castle;
    }

    /**
     * set castle flag
     * @param castle boolean
     */
    public void setCastle(boolean castle) {
        this.castle = castle;
    }

    /**
     * @return the spot the king goes to if it castle
     */
    public String getCastleLanding() {
        return castleLanding;
    }

    /**
     * set castle landing spot
     * @param castleLanding Space
     */
    public void setCastleLanding(String castleLanding) {
        this.castleLanding = castleLanding;
    }

    /**
     * implementation of inherited abstract method for calculating moves
     */
    public void calculateMoves() {
        Space curSpace = getSpace();
        String [] tmpMoves;
        String [] basicMoves;
        if(isCastle())
            basicMoves = new String[10];
        else
            basicMoves = new String[9];
        char column = curSpace.getName().charAt(0);
        int row = Character.getNumericValue(curSpace.getName().charAt(1));
        char tmp = --column;
        ++column;
        char max = ++column;
        --column;
        int tmpMovesSize = 0;
        int index = 0;
        for(char a = tmp; a <= max; a++) {
            for(int i = row - 1; i <= row + 1; i++) {
                if(a < 'a' || i < 1 || a > 'h' || i > 8 || (a == column && i == row)) {
                    basicMoves[index++] = "no";
                }
                else {
                    String m = a + Integer.toString(i);
                    basicMoves[index++] = m;
                    tmpMovesSize++;
                }
            }
        }
        if(isCastle()) {
            basicMoves[9] = getCastleLanding();
            tmpMoves = new String[tmpMovesSize+1];
        }
        else {
            tmpMoves = new String[tmpMovesSize];
        }

        int index2 = 0;
        for(int i = 0; i < basicMoves.length; i++) {
            if(!(basicMoves[i].equals("no"))){
                tmpMoves[index2++] = basicMoves[i];
            }
        }

        String [] moves = cleanMoves(tmpMoves);
        setMoves(moves);
        if(Board.getInCheck()) {
            if(getMoves().length > 0) {
                for(int i = 0; i < getMoves().length; i++) {
                    String okayMove = this.getSpace().getName() + " " + getMoves()[i];
                    if(getType().charAt(0) == 'w')
                        Board.WsetGoodInCheckMoves(okayMove);
                    else
                        Board.BsetGoodInCheckMoves(okayMove);
                }
            }
            boolean cMate = true;
            List<String> targetMoves = new ArrayList<String>();
            Piece [] wPieces = Board.getWhitePieces();
            Piece [] bPieces = Board.getBlackPieces();
            boolean turn = (getType().charAt(0) == 'w' ? true : false);

            if(turn) {
                for(int i = 0; i < bPieces.length; i++) {
                    if(bPieces[i].isiHaveCheck()) {
                        targetMoves.add(bPieces[i].getSpace().getName());
                        for(int j = 0; j < bPieces[i].getPathToKing().length; j++) {
                            targetMoves.add(bPieces[i].getPathToKing()[j]);
                        }
                    }
                }

                for(int i = 0; i < wPieces.length; i++) {
                    for(int j = 0; j < wPieces[i].getMoves().length; j++) {
                        if(targetMoves.contains(wPieces[i].getMoves()[j])) {
                            String okayMove = wPieces[i].getSpace().getName() + " " + wPieces[i].getMoves()[j];
                            Board.WsetGoodInCheckMoves(okayMove);
                            cMate = false;
                        }
                    }
                }

                if(cMate && Board.WgetGoodInCheckMoves().size() == 0) {
                    Board.setCheckMate(true);
                }
            }
            else {
                for(int i = 0; i < wPieces.length; i++) {
                    if(wPieces[i].isiHaveCheck()) {
                        targetMoves.add(wPieces[i].getSpace().getName());
                        for(int j = 0; j < wPieces[i].getPathToKing().length; j++) {
                            targetMoves.add(wPieces[i].getPathToKing()[j]);
                        }
                    }
                }
                for(int i = 0; i < bPieces.length; i++) {
                    for(int j = 0; j < bPieces[i].getMoves().length; j++) {
                        if(targetMoves.contains(bPieces[i].getMoves()[j])) {
                            String okayMove = bPieces[i].getSpace().getName() + " " + bPieces[i].getMoves()[j];
                            Board.BsetGoodInCheckMoves(okayMove);
                            cMate = false;
                        }
                    }
                }

                if(cMate && Board.BgetGoodInCheckMoves().size() == 0) {
                    Board.setCheckMate(true);
                }
            }
        }
    }

    /**
     * After calculating the basic moves they need to be clean of spaces that are occupied by pieces of the same color or blocking pieces
     * @param bmoves [] Strings
     * @return array of clean move set
     */
    public String [] cleanMoves(String [] bmoves) {
        for(int i = 0; i < bmoves.length; i++) {
            if(movingToCheck(bmoves[i])) {
                bmoves[i] = "no";
                continue;
            }
            Space tmp = Board.fetchSpace(bmoves[i]);
            if(tmp.getPiece() != null) {
                if((tmp.getPiece().getType().charAt(0) == this.getType().charAt(0))) {
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
     * When cleaning moves make sure noe of the spots would move the king into check
     * @param test String
     * @return boolean if it would be in check or not
     */
    public boolean movingToCheck(String test) {
        Piece [] pieces = ((getType().charAt(0) == 'w') ? Board.getBlackPieces() : Board.getWhitePieces());
        for(int i = 0; i < pieces.length; i++) {
            if(pieces[i] == null)
                return false;
        }
        for(int i = 0; i < pieces.length; i++) {
            String [] pieceMoves = pieces[i].getMoves();
            for(int j = 0; j < pieceMoves.length; j++) {
                if(test.equals(pieceMoves[j]) && pieces[i].getType().charAt(1) != 'p')
                    return true;
            }
            if(pieces[i].getType().charAt(1) == 'p'){
                if(pieces[i].getType().charAt(0) == 'w'){
                    Pawn tmp = (Pawn) pieces[i];
                    Space [] corners = tmp.getCorners();
                    if(test.equals(corners[0].getName()) || test.equals(corners[2].getName()))
                        return true;
                }
                if(pieces[i].getType().charAt(0) == 'b'){
                    Pawn tmp = (Pawn) pieces[i];
                    Space [] corners = tmp.getCorners();
                    if(test.equals(corners[1].getName()) || test.equals(corners[3].getName()))
                        return true;
                }
            }
        }
        return false;
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
