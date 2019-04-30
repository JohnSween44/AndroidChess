package com.example.androidchess;


import android.widget.ImageView;

/**
 *
 * @author John Sweeney
 * @author Denny Sabu
 *
 */

public class Space {
    /*
     * Class for spaces that the board is built out of
     * Fields
     * 	-name: name of the space (String)
     * 	-color: whether the space default displays "  " or "##" (String)
     *  -piece: piece that is currently occupying the space if any (Piece)
     */
    private String name;
    private String color;
    private Piece piece;
    private ImageView spot;

    /**
     * Constructor for building a space
     * @param color String
     * @param name String
     */
    public Space(String color, String name, ImageView resource) {
        setSpot(resource);
        setColor(color);
        setName(name);
    }

    public Space(Space space){
        this(space.getColor(), space.getName(), space.getSpot());
    }

    /**
     * set the color
     * @param color string
     */
    public void setColor(String color) {
        this.color = color;
    }

    public void setSpot(ImageView spot){
        this.spot = spot;
    }

    /**
     * set the name
     * @param name String
     */
    public void setName (String name) {
        this.name = name;
    }

    /**
     * set the piece that is on this space
     * @param piece Piece
     */
    public void setPiece(Piece piece) {
        if(piece == null) {
            this.piece = null;
            getSpot().setImageDrawable(null);
        }
        else {
            this.piece = piece;
            getSpot().setImageDrawable(piece.getSprite());
        }
    }

    /**
     * If a piece wants to move to this space it must check if this space is in the move set of the piece
     * if so this space's piece is set and the space of the piece is set
     * @param piece Piece
     * @return boolean if the piece can legally move here
     */
    public boolean movePiece(Piece piece) {
        if(piece.checkMoves(this)) {
            this.piece = piece;
            getSpot().setImageDrawable(piece.getSprite());
            piece.setHasItMovedYet(true);
            piece.setSpace(this);
            return true;
        }
        else
            return false;
    }

    /**
     * get the name
     * @return name
     */
    public String getName() {
        return name;
    }

    public ImageView getSpot(){
        return spot;
    }

    /**
     * get the color
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * get the piece
     * @return piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Overridden to String, if there is a piece on this space then it prints the piece type, if not then its color
     */
    public String toString() {
        if(piece == null)
            return color;
        else
            return piece.toString();
    }

    /**
     * Overridden equals is to see if too spaces are the same
     */
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Space)) {
            return false;
        }

        Space s = (Space) o;

        if(s.getName().equals(this.getName()))
            return true;
        else
            return false;
    }
}