package com.example.androidchess;

import android.graphics.drawable.Drawable;

import java.util.*;

/**
 *
 * @author John Sweeney
 * @author Denny Sabu
 *
 */

public class Pawn extends Piece{
    /**
     * Pawn class that functions for both the white and black pawns
     * Fields
     *  -Direction: to see which way the pawn is faceting based off its color (Boolean)
     *  -passSpot: the spot two spaces away from a pawns starting spot (String)
     *  -enpassant: if the pawns first move is to its pass spot flag it for enpassant(Boolean)
     *  -passOpertun: spot next to a space with a pawn whose enpassant flag is true (String)
     *  -passTake: spot the pawn would go to if it executes enpassant(String)
     *  -TargetPawnSpot: space with enpassant pawn in it (Space)
     *  -spots:list to see if first move was to passSpot (list<String>)
     *
     */
    private boolean pawnPromotion;
    private boolean direction;
    private String passSpot;
    private boolean enpassant;
    private String passOpertun;
    private String passTake;
    private Space targetPawnSpot;
    private List<String> spots = new ArrayList<String>();

    /**
     * Constructor for pawn
     * @param type String
     * @param space Space
     */
    public Pawn(String type, Space space, Drawable x) {
        super(type, space, x);
    }

    /**
     * Overridden setSpace() from that also checks for enpassant opportunities before setting the space
     */
    public void setSpace(Space space) {
        if(this.getSpace() != null) {
            if(this.getSpace().getName().equals(getPassOpertun()) && space.getName().equals(getPassTake())) {
                Pawn tmpPawn = (Pawn)getTargetPawnSpot().getPiece();
                tmpPawn.endSelf();
                Board.managePieces(tmpPawn);
            }
            else {
                this.setPassOpertun(null);
                this.setPassTake(null);
                this.setTargetPawnSpot(null);
            }
            spots.add(this.getSpace().getName());
            spots.add(space.getName());
        }
        super.setSpace(space);
    }

    /**
     * @return passOpertun
     */
    public String getPassOpertun() {
        return passOpertun;
    }

    /**
     * set passOpertun
     * @param passOpertun String
     */
    public void setPassOpertun(String passOpertun) {
        this.passOpertun = passOpertun;
    }

    /**
     * @return passTake
     */
    public String getPassTake() {
        return passTake;
    }

    /**
     * @return getTargetPawnSpot (Space)
     */
    public Space getTargetPawnSpot() {
        return targetPawnSpot;
    }

    /**
     * set targetPawnSpot
     * @param targetPawnSpot Space
     */
    public void setTargetPawnSpot(Space targetPawnSpot) {
        this.targetPawnSpot = targetPawnSpot;
    }

    /**
     * set passTake
     * @param passTake String
     */
    public void setPassTake(String passTake) {
        this.passTake = passTake;
    }

    /**
     * check if pawn has set itself up for ennpassant
     */
    private void enPassCheck() {
        if(this.getSpace() != null) {
            if(getSpace().getName().equals(getPassSpot()) && spots.size() == 2) {
                setEnpassant(true);
            }
            else {
                setEnpassant(false);
                setPassSpot(null);
            }
        }
    }

    /**
     * implementation of inherited abstract method for calculating moves
     */
    public void calculateMoves() {
        enPassCheck();

        if(getType().charAt(0) == 'b')
            direction = false;
        else
            direction = true;

        Space curSpace = getSpace();
        char column = curSpace.getName().charAt(0);
        int row = Character.getNumericValue(curSpace.getName().charAt(1));
        String [] tmpMoves;

        List<String> moves = new ArrayList<String>();
        Space [] corners = new Space [4];
        char c = ++column;
        --column;
        String m1 = c + Integer.toString(row + 1);
        String m2 = c + Integer.toString(row - 1);
        corners[0] = Board.fetchSpace(m1);
        corners[1] = Board.fetchSpace(m2);
        char f = --column;
        ++column;
        String m3 = f + Integer.toString(row + 1);
        String m4 = f + Integer.toString(row - 1);
        corners[2] = Board.fetchSpace(m3);
        corners[3] = Board.fetchSpace(m4);
        if(direction) {
            if(corners[0].getPiece() != null){
                if(corners[0].getPiece().getType().charAt(0) != this.getType().charAt(0)) {
                    moves.add(corners[0].getName());
                }
            }
            if(corners[2].getPiece() != null){
                if(corners[2].getPiece().getType().charAt(0) != this.getType().charAt(0)) {
                    moves.add(corners[2].getName());
                }
            }
        }
        else {
            if(corners[1].getPiece() != null){
                if(corners[1].getPiece().getType().charAt(0) != this.getType().charAt(0)) {
                    moves.add(corners[1].getName());
                }
            }
            if(corners[3].getPiece() != null){
                if(corners[3].getPiece().getType().charAt(0) != this.getType().charAt(0)) {
                    moves.add(corners[3].getName());
                }
            }
        }

        String sideA = ((direction) ? ++column : --column) + Integer.toString(row);
        if(direction)
            --column;
        else
            ++column;
        String sideB = ((direction) ? --column : ++column) + Integer.toString(row);
        if(direction)
            ++column;
        else
            --column;

        if(Board.fetchSpace(sideA).getPiece() != null &&
                this.getType().charAt(0) != Board.fetchSpace(sideA).getPiece().getType().charAt(0) &&
                Board.fetchSpace(sideA).getPiece().getType().charAt(1) == 'p') {
            Pawn tmpPawn = (Pawn)Board.fetchSpace(sideA).getPiece();

            if(tmpPawn.isEnpassant() && ((direction) ? corners[0].getPiece() == null : corners[3].getPiece() == null)) {
                moves.add((direction) ? corners[0].getName() : corners[3].getName());
                setPassOpertun(this.getSpace().getName());
                setPassTake((direction) ? corners[0].getName() : corners[3].getName());
                setTargetPawnSpot(Board.fetchSpace(sideA));
            }
        }

        if(Board.fetchSpace(sideB).getPiece() != null &&
                this.getType().charAt(0) != Board.fetchSpace(sideB).getPiece().getType().charAt(0) &&
                Board.fetchSpace(sideB).getPiece().getType().charAt(1) == 'p') {
            Pawn tmpPawn = (Pawn)Board.fetchSpace(sideB).getPiece();

            if(tmpPawn.isEnpassant() && (direction) ? corners[2].getPiece() == null : corners[1].getPiece() == null) {
                moves.add((direction) ? corners[2].getName() : corners[1].getName());
                setPassOpertun(this.getSpace().getName());
                setPassTake((direction) ? corners[2].getName() : corners[1].getName());
                setTargetPawnSpot(Board.fetchSpace(sideB));
            }
        }

        if(!getHasItMovedYet()) {
            String m8 = column + Integer.toString((direction) ? ++row : --row);
            if(Board.fetchSpace(m8).getPiece() == null)
                moves.add(m8);
            String m9 = column + Integer.toString((direction) ? ++row : --row);
            setPassSpot(m9);
            if(Board.fetchSpace(m9).getPiece() == null)
                moves.add(m9);
            tmpMoves = new String[moves.size()];
            tmpMoves = moves.toArray(tmpMoves);
            setMoves(tmpMoves);
        }
        else {
            String m5 = column + Integer.toString((direction) ? ++row : --row);
            if(Board.fetchSpace(m5).getPiece() == null)
                moves.add(m5);
            tmpMoves = new String[moves.size()];
            tmpMoves = moves.toArray(tmpMoves);
            setMoves(tmpMoves);
        }
    }

    /*
     * If enppassant happens to this Pawn it destroys itself
     */
    public void endSelf() {
        this.getSpace().setPiece(null);
    }

    /**
     * Implementation of inherited abstract  method from piece for calculating the path to the king
     */
    public void calcPathToKing(Space kingSpot) {
        String [] path = new String[1];
        path[0] = kingSpot.getName();
        setPathToKing(path);
    }

    /**
     * @return passSpot
     */
    public String getPassSpot() {
        return passSpot;
    }

    /**
     * Set passSpot
     * @param passSpot String
     */
    public void setPassSpot(String passSpot) {
        this.passSpot = passSpot;
    }

    /**
     * @return enpassant flag
     */
    public boolean isEnpassant() {
        return enpassant;
    }

    /**
     * set enpassant flag
     * @param enpassant boolean
     */
    public void setEnpassant(boolean enpassant) {
        this.enpassant = enpassant;
    }

    /**
     * set promotion
     * @param z boolean
     */
    public void setPromotion(boolean z) {
        pawnPromotion = z;
    }

    /**
     * @return pawnPromotion
     */
    public boolean getPromotion() {
        return pawnPromotion;
    }

    /**
     * @return direction
     */
    public boolean getDirection() {
        return direction;
    }
}
