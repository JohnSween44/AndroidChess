package com.example.androidchess;


import java.util.*;

/**
 * @author John Sweeney
 * @author Denny Sabu
 *
 */

public abstract class Piece {
    /**
     * Abstract Class that all pieces inhere since all pieces are all the same except when calculating where they can move
     * Fields
     *  -type: what type the pieces (String)
     *  -space: current space of the piece (Space)
     *  -moves[]: array of spots the piece can move too (String)
     *  -pathToKing[]: if the piece has a king in check what are the spaces to the king (String)
     *  -rangeOfKing[]: if the piece has a king in check and the piece could move to spots behind the
     *  	they are stored in here so that the king cant move into check in these spots (String)
     *  -hasItMoved: if the piece has moved yet from its starting position(Boolean)
     *  -iHaveCheck: flag to set if a space in moves[] has the opponets king in it
     */
    private String type;
    private Space space;
    private String [] moves;
    private String [] pathToKing;
    private String [] rangeOfCheck;
    private boolean hasItMovedYet = false;
    private boolean iHaveCheck = false;

    /**
     * Constructor for Piece
     * @param type String
     * @param space Space
     */
    public Piece(String type, Space space) {
        setType(type);
        setSpace(space);
    }

    /**
     * set they type of pawn with 2 character string where the first character is the color of the piece and the second is the kind of piece
     * @param type String
     */
    public void setType(String type) {
        List<String> types = new ArrayList<String>();
        types.add("bR"); types.add("bN"); types.add("bB"); types.add("bQ");
        types.add("bK"); types.add("bp"); types.add("wR"); types.add("wN");
        types.add("wB"); types.add("wQ"); types.add("wK"); types.add("wp");

        if(types.contains(type))
            this.type = type;
        else
            System.out.println("invalid piece type");
    }

    /**
     * sets the space and calls the abstract method for calculating the move set of the given piece
     * @param space Space
     */
    public void setSpace(Space space) {
        this.space = space;
        calculateMoves();
    }

    /**
     * Once the moves are calculated they are set here and the signalCheckMethod is called to
     * check if the new move set contains an enemy King
     * @param moves [] array of strings
     */
    public void setMoves(String [] moves) {
        this.moves = moves;
        signalCheck();
    }

    /**
     * set the hasItMovedYet flag
     * @param x Boolean to set flag too
     */
    public void setHasItMovedYet(boolean x) {
        hasItMovedYet = x;
    }

    /**
     * get if this piece has a king in check
     * @return boolean iHaveCheck
     */
    public boolean isiHaveCheck() {
        return iHaveCheck;
    }

    /**
     * set iHaveVheck flag if piece has a king in check
     * @param iHaveCheck boolean
     */
    public void setiHaveCheck(boolean iHaveCheck) {
        this.iHaveCheck = iHaveCheck;
    }

    /**
     * @return array of range of spots to king if the piece has a king in check
     */
    public String[] getRangeOfCheck() {
        return rangeOfCheck;
    }

    /**
     * set the array of range of spots to king if piece has king in check
     * @param rangeOfCheck String[]
     */
    public void setRangeOfCheck(String[] rangeOfCheck) {
        this.rangeOfCheck = rangeOfCheck;
    }

    /**
     * check if the space is in the move set of the piece
     * @param space Space
     * @return boolean if it is a legal move
     */
    public boolean checkMoves(Space space) {
        if(space == this.space)
            return true;
        for(int i = 0; i < moves.length; i++) {
            if(space.getName().equals(moves[i]))
                return true;
        }
        return false;
    }

    /**
     * Method to runs through all the moves to see if there is an enemy king in any of the spaces, is set the iHaveChack flag
     * and calls calculating the path to king
     */
    public void signalCheck() {
        for(int i = 0; i < moves.length; i++) {
            Space tmp = Board.fetchSpace(moves[i]);
            if(tmp.getPiece() != null) {
                if(this.getType().charAt(0) != tmp.getPiece().getType().charAt(0) && tmp.getPiece().getType().charAt(1) == 'K') {
                    setiHaveCheck(true);
                    calcPathToKing(tmp);
                    break;
                }
                else {
                    setiHaveCheck(false);
                }
            }
        }
    }

    /**
     * get the move set of the piece
     * @return move [] string
     */
    public String [] getMoves() {
        return moves;
    }

    /**
     * get current space of piece
     * @return space
     */
    public Space getSpace() {
        return space;
    }

    /**
     * get type of the piece
     * @return string
     */
    public String getType() {
        return type;
    }

    /**
     * gets path to king that the piece has in check
     * @return pathToKing[]
     */
    public String[] getPathToKing() {
        return pathToKing;
    }

    /**
     * set the path to the king
     * @param pathToKing String[]
     */
    public void setPathToKing(String[] pathToKing) {
        this.pathToKing = pathToKing;
    }

    /**
     * returns the hasItMoved flag
     * @return boolean
     */
    public boolean getHasItMovedYet() {
        return hasItMovedYet;
    }
    /**
     * abstract method for calculating moves
     */
    public abstract void calculateMoves();
    /**
     * abstract method for calculating
     * @param kingSpot (Space)
     */
    public abstract void calcPathToKing(Space kingSpot);

    /**
     * overridden toString method that prints the type of piece
     */
    public String toString() {
        return type;
    }

    /**
     * Overridden equals method that compares the spaces of the pieces to see if there the same piece
     */
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Piece)) {
            return false;
        }

        Piece p = (Piece) o;

        if(p.getSpace().equals(this.getSpace()))
            return true;
        else
            return false;
    }
}
