package com.example.androidchess;

import android.app.Activity;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author John Sweeney
 * @author Denny Sabu
 *
 */

public class Board {
    /**
     * The fields of the BOard class
     * 	-2D array of spaces for board
     *  -Arrays of white pieces and black pieces
     *  -a promotion string for type of piece to promote pawns too
     *  -legal moves for white and black pieces are in check
     *  -flags for check and checkmate
     *  -flag to detect if players want to draw
     *  -int for determining whose turn it is
     *  -scanner to read in user moves
     * Most of these fields are static as there should only ever be one instance of
     * the board
     */
    private static Space [][] board = new Space[8][8];
    private static Space [][] secondBoard = new Space[8][8];
    private static Piece [] whitePieces = new Piece[16];
    private static List<Piece> backUpWhitePieces = new ArrayList<Piece>();
    private static Piece [] blackPieces = new Piece[16];
    private static List<Piece> backupBlackPieces = new ArrayList<Piece>();
    private static String promotion = "Q";
    private static List<String> WgoodInCheckMoves = new ArrayList<String>();
    private static List<String> BgoodInCheckMoves = new ArrayList<String>();
    private static boolean inCheck = false;
    private static boolean checkMate = false;
    private static boolean drawFlag = false;
    private static int whoseTurn = 0;
    private Activity x;
    //private Scanner keys = new Scanner(System.in);

    /**
     * Constructor builds board and creates pieces
     */
    public Board(Activity activity) {
        reset();
        x = activity;
        int whiteIndex = 0;
        int blackIndex = 0;
        int colorPicker = 0;
        int row = 8;
        for(int i = 0; i < 8; i++) {
            char column = 'a';
            for(int j = 0; j < 8; j++) {
                String name = column + Integer.toString(row);
                String color = ((colorPicker % 2 == 0) ? "  " : "##");
                int resID = x.getResources().getIdentifier(name, "id", x.getPackageName());
                Space s = new Space(color, name, (ImageView)x.findViewById(resID));
                board[i][j] = s;
                column++;
                colorPicker++;
            }
            colorPicker--;
            row--;
        }

        for(int i = 0; i < 8; i++) {
            Pawn blackP = new Pawn("bp", board[1][i], x.getResources().getDrawable(R.drawable.bpawn, x.getTheme()));
            board[1][i].setPiece(blackP);
            blackPieces[blackIndex++] = blackP;
            Pawn whiteP = new Pawn("wp", board[6][i], x.getResources().getDrawable(R.drawable.wpawn, x.getTheme()));
            board[6][i].setPiece(whiteP);
            whitePieces[whiteIndex++] = whiteP;
        }

        Rook bRook1 = new Rook("bR", board[0][0], x.getResources().getDrawable(R.drawable.brook, x.getTheme()));
        Rook bRook2 = new Rook("bR", board[0][7], x.getResources().getDrawable(R.drawable.brook, x.getTheme()));
        board[0][0].setPiece(bRook1);
        blackPieces[blackIndex++] = bRook1;
        board[0][7].setPiece(bRook2);
        blackPieces[blackIndex++] = bRook2;
        Rook wRook1 = new Rook("wR", board[7][0], x.getResources().getDrawable(R.drawable.wrook, x.getTheme()));
        Rook wRook2 = new Rook("wR", board[7][7], x.getResources().getDrawable(R.drawable.wrook, x.getTheme()));
        board[7][0].setPiece(wRook1);
        whitePieces[whiteIndex++] = wRook1;
        board[7][7].setPiece(wRook2);
        whitePieces[whiteIndex++] = wRook2;
        Knight bKnight1 = new Knight("bN", board[0][1], x.getResources().getDrawable(R.drawable.bknight, x.getTheme()));
        Knight bKnight2 = new Knight("bN", board[0][6], x.getResources().getDrawable(R.drawable.bknight, x.getTheme()));
        board[0][1].setPiece(bKnight1);
        blackPieces[blackIndex++] = bKnight1;
        board[0][6].setPiece(bKnight2);
        blackPieces[blackIndex++] = bKnight2;
        Knight wKnight1 = new Knight("wN", board[7][1], x.getResources().getDrawable(R.drawable.wknight, x.getTheme()));
        Knight wKnight2 = new Knight("wN", board[7][6], x.getResources().getDrawable(R.drawable.wknight, x.getTheme()));
        board[7][1].setPiece(wKnight1);
        whitePieces[whiteIndex++] = wKnight1;
        board[7][6].setPiece(wKnight2);
        whitePieces[whiteIndex++] = wKnight2;
        Bishop bBishop1 = new Bishop("bB", board[0][2], x.getResources().getDrawable(R.drawable.bbishop, x.getTheme()));
        Bishop bBishop2 = new Bishop("bB", board[0][5], x.getResources().getDrawable(R.drawable.bbishop, x.getTheme()));
        board[0][2].setPiece(bBishop1);
        blackPieces[blackIndex++] = bBishop1;
        board[0][5].setPiece(bBishop2);
        blackPieces[blackIndex++] = bBishop2;
        Bishop wBishop1 = new Bishop("wB", board[7][2], x.getResources().getDrawable(R.drawable.wbishop, x.getTheme()));
        Bishop wBishop2 = new Bishop("wB", board[7][5], x.getResources().getDrawable(R.drawable.wbishop, x.getTheme()));
        board[7][2].setPiece(wBishop1);
        whitePieces[whiteIndex++] = wBishop1;
        board[7][5].setPiece(wBishop2);
        whitePieces[whiteIndex++] = wBishop2;
        Queen bQueen = new Queen("bQ", board[0][3], x.getResources().getDrawable(R.drawable.bqueen, x.getTheme()));
        Queen wQueen = new Queen("wQ", board[7][3], x.getResources().getDrawable(R.drawable.wqueen, x.getTheme()));
        board[0][3].setPiece(bQueen);
        blackPieces[blackIndex++] = bQueen;
        board[7][3].setPiece(wQueen);
        whitePieces[whiteIndex++] = wQueen;
        King bKing = new King("bK", board[0][4], x.getResources().getDrawable(R.drawable.bking, x.getTheme()));
        King wKing = new King("wK", board[7][4], x.getResources().getDrawable(R.drawable.wking, x.getTheme()));
        board[0][4].setPiece(bKing);
        blackPieces[blackIndex++] = bKing;
        board[7][4].setPiece(wKing);
        whitePieces[whiteIndex++] = wKing;
        reState();
        //print();
    }

    public static void reset(){
        whoseTurn = 0;
        drawFlag = false;
        checkMate = false;
        inCheck = false;
        BgoodInCheckMoves = new ArrayList<String>();
        WgoodInCheckMoves = new ArrayList<String>();
        backupBlackPieces = new ArrayList<Piece>();
        blackPieces = new Piece[16];
        backUpWhitePieces = new ArrayList<Piece>();
        whitePieces = new Piece[16];
    }

    /**
     * Starts the game/move by parsing user input to determine if there castle-ing,
     * promoting, drawing, resigning, and if the move is legal and can be cared out. Also determining if the
     * the game has ended
     * @return Returns boolean true always until there is a winner
     * 		   in which case it returns false as the game has now ended
     */
    public int play(String input) {
        backUp();
        //print();
        Piece inDangerPiece = null;
        boolean possibleCapture = false;
        //String input = keys.nextLine();
        //System.out.println("");
        String [] inputs = input.split(" ");
        /*
        if(inputs.length > 3 || inputs.length < 1) {
            System.out.print("Illegal move, try again: ");
            return true;
        }
        */
        if(inputs.length == 1) {
            if(inputs[0].equals("resign")) {
                //String winner = ((whoseTurn % 2 == 0) ? "Black wins" : "White wins");
                //System.out.println(winner);
                return 1;
            }
            else if(inputs[0].equals("draw") && drawFlag) {
                return 2;
            }
            else if(inputs[0].equals("draw")){
                drawFlag = true;
                return 4;
            }
            else {
                //System.out.print("Illegal move, try again: ");
                return -1;
            }
        }

        String target = inputs[0];
        String destination = inputs[1];

        if(target.equals(destination)) {
            //System.out.print("Illegal move, try again: ");
            return -1;
        }
        if(target.charAt(0) < 'a' || target.charAt(0) > 'h' || target.charAt(1) > '8' || target.charAt(1) < '1') {
            //System.out.print("Illegal move, try again: ");
            return -1;
        }
        if(destination.charAt(0) < 'a' || destination.charAt(0) > 'h' || destination.charAt(1) > '8' || destination.charAt(1) < '1') {
            //System.out.print("Illegal move, try again: ");
            return -1;
        }

        Space tar = Board.fetchSpace(target);
        Space dest = Board.fetchSpace(destination);

        if(tar.getPiece() == null) {
            //System.out.print("Illegal move, try again: ");
            return -1;
        }
        if((tar.getPiece().getType().charAt(0) == 'b' && whoseTurn % 2 == 0) || (tar.getPiece().getType().charAt(0) == 'w' && whoseTurn % 2 != 0)) {
            //System.out.print("Illegal move, try again: ");
            return -1;
        }

        if(tar.getPiece() != null && dest.getPiece() != null) {
            if(tar.getPiece().getType().charAt(0) == dest.getPiece().getType().charAt(0)) {
                //System.out.print("Illegal move, try again: ");
                return -1;
            }
        }

        if((whoseTurn % 2 == 0) ? ((tar.getName().equals("e1") && dest.getName().equals("g1")) || (tar.getName().equals("e1") && dest.getName().equals("c1"))) :
                ((tar.getName().equals("e8") && dest.getName().equals("g8")) || (tar.getName().equals("e8") && dest.getName().equals("c8")))) {

            if(inCheck) {
                //System.out.print("Illegal move, try again: ");
                return -1;
            }
            if(tar.getPiece() == null || tar.getPiece().getHasItMovedYet()) {
                //System.out.print("Illegal move, try again: ");
                return -1;
            }

            Space rookSpace;
            Space rookLanding;
            switch(dest.getName()) {
                case "g1":
                    rookSpace = Board.fetchSpace("h1");
                    rookLanding = Board.fetchSpace("f1");
                    break;
                case "c1":
                    rookSpace = Board.fetchSpace("a1");
                    rookLanding = Board.fetchSpace("d1");
                    break;
                case "g8":
                    rookSpace = Board.fetchSpace("h8");
                    rookLanding = Board.fetchSpace("f8");
                    break;
                case "c8":
                    rookSpace = Board.fetchSpace("a8");
                    rookLanding = Board.fetchSpace("d8");
                    break;
                default:
                    rookSpace = null;
                    rookLanding = null;
            }

            if(rookSpace.getPiece() == null || rookSpace.getPiece().getHasItMovedYet()) {
                //System.out.print("Illegal move, try again: ");
                return -1;
            }

            Space inbetweenSpotA;
            Space inbetweenSpotB;
            Space inbetweenSpotC;
            boolean rightSide;

            switch(input) {
                case "e1 g1":
                    inbetweenSpotA = Board.fetchSpace("f1");
                    inbetweenSpotB = Board.fetchSpace("g1");
                    inbetweenSpotC = null;
                    rightSide = true;
                    break;
                case "e1 c1":
                    inbetweenSpotA = Board.fetchSpace("d1");
                    inbetweenSpotB = Board.fetchSpace("c1");
                    inbetweenSpotC = Board.fetchSpace("b1");
                    rightSide = false;
                    break;
                case "e8 g8":
                    inbetweenSpotA = Board.fetchSpace("f8");
                    inbetweenSpotB = Board.fetchSpace("g8");
                    inbetweenSpotC = null;
                    rightSide = true;
                    break;
                case "e8 c8":
                    inbetweenSpotA = Board.fetchSpace("d8");
                    inbetweenSpotB = Board.fetchSpace("c8");
                    inbetweenSpotC = Board.fetchSpace("b8");
                    rightSide = false;
                    break;
                default:
                    System.out.println("not good");
                    inbetweenSpotA = null;
                    inbetweenSpotB = null;
                    inbetweenSpotC = null;
                    rightSide = false;
            }

            if(rightSide ? inbetweenSpotA.getPiece() != null || inbetweenSpotB.getPiece() != null :
                    inbetweenSpotA.getPiece() != null || inbetweenSpotB.getPiece() != null || inbetweenSpotC.getPiece() != null) {
                //System.out.print("Illegal move, try again: ");
                return -1;
            }

            List<Space> inbetweeners = new ArrayList<Space>();
            inbetweeners.add(inbetweenSpotA);
            inbetweeners.add(inbetweenSpotB);
            inbetweeners.add(inbetweenSpotC);

            for(Space p : inbetweeners) {
                if(p == null)
                    continue;
                Piece [] pieces = ((whoseTurn % 2 == 0) ? getBlackPieces() : getWhitePieces());
                for(int i = 0; i < pieces.length; i++) {
                    for(int j = 0; j < pieces[i].getMoves().length; j++) {
                        if(p.getName().equals(pieces[i].getMoves()[j])) {
                            //System.out.print("Illegal move, try again: ");
                            return -1;
                        }
                    }
                }
            }

            King tmpKing = (King)tar.getPiece();
            tmpKing.setCastle(true);
            tmpKing.setCastleLanding(dest.getName());
            tmpKing.calculateMoves();
            Rook tmpRook = (Rook)rookSpace.getPiece();
            if(dest.movePiece(tmpKing)) {
                tar.setPiece(null);
            }
            else {
                System.out.println("didnt work");
            }

            if(rookLanding.movePiece(tmpRook)) {
                rookSpace.setPiece(null);
            }
            else {
                System.out.println("didnt work");
            }

            tmpKing.setCastle(false);

            checkPromotion((whoseTurn % 2 == 0) ? true : false);
            Board.setInCheck(false);
            Board.setCheckMate(false);
            reState();
            reState();
            whoseTurn++;
            //print();
            if(isCheckMate())
                return 0;

            return 3;

        }

        if(inputs.length == 3) {
            String thirdOption = inputs[2];
            if(tar.getPiece() instanceof Pawn &&
                    thirdOption.equals("R") || thirdOption.equals("N")  ||
                    thirdOption.equals("B") || thirdOption.equals("Q")) {

                if(tar.getPiece().getType().charAt(0) == 'b' && dest.getName().charAt(1) == '1') {
                    promotion = thirdOption;
                }
                else if(tar.getPiece().getType().charAt(0) == 'w' && dest.getName().charAt(1) == '8') {
                    promotion = thirdOption;
                }
                else {
                    //System.out.print("Illegal move, try again: ");
                    return -1;
                }

            }
            else {
                //System.out.print("Illegal move, try again: ");
                return -1;
            }

        }
        if(dest.getPiece() != null) {
            inDangerPiece = dest.getPiece();
            possibleCapture = true;
        }
        if(inCheck) {
            if((whoseTurn % 2 == 0) ? Board.WgoodInCheckMoves.size() == 0 : Board.BgoodInCheckMoves.size() == 0) {
                setCheckMate(true);
                //print();
                if(isCheckMate())
                    return 0;
            }
            else if((whoseTurn % 2 == 0) ? Board.WgoodInCheckMoves.contains(input) : Board.BgoodInCheckMoves.contains(input)) {
                if(dest.movePiece(tar.getPiece())) {
                    tar.setPiece(null);
                    if(possibleCapture) {
                        managePieces(inDangerPiece);
                    }
                    checkPromotion((whoseTurn % 2 == 0) ? true : false);
                    Board.setInCheck(false);
                    Board.setCheckMate(false);
                    reState();
                    reState();
                    whoseTurn++;
                    //print();
                    if(isCheckMate())
                        return 0;

                    return 3;
                }
                else {
                    String [] m = tar.getPiece().getMoves();
                    //System.out.println(Arrays.toString(m));
                   // System.out.print("Illegal move, try again: ");
                    return -1;
                }
            }
            else {
                //System.out.print("Illegal move, try again: ");
                return -1;
            }
        }
        if(dest.movePiece(tar.getPiece())) {
            tar.setPiece(null);
            if(possibleCapture) {
                managePieces(inDangerPiece);
            }
            checkPromotion((whoseTurn % 2 == 0) ? true : false);
            Board.setInCheck(false);
            Board.setCheckMate(false);
            reState();
            reState();
            whoseTurn++;
            //print();
            if(isCheckMate())
                return 0;

            return 3;
        }
        else {
            String [] m = tar.getPiece().getMoves();
            //System.out.print("Illegal move, try again: ");
            return -1;
        }
    }

    public static int getWhoseTurn(){
        return whoseTurn;
    }

    /**
     * If a piece is captured it must be removed from the board and this Array that contains the piece to be terminated
     * @param deadPiece is the piece to be removed
     */
    public static void managePieces(Piece deadPiece) {
        int newIndex = 0;
        Piece [] oldPieces = ((deadPiece.getType().charAt(0) == 'w') ? getWhitePieces() : getBlackPieces());
        Piece [] newPieces = new Piece[oldPieces.length - 1];
        for(int i = 0; i < oldPieces.length; i++) {
            if(!(oldPieces[i].getSpace().equals(deadPiece.getSpace()))) {
                newPieces[newIndex++] = oldPieces[i];
            }
        }
        if(deadPiece.getType().charAt(0) == 'w')
            whitePieces = newPieces;
        else
            blackPieces = newPieces;
    }
    /**
     * Static method for pieces to see the board
     * @return 2D Array of spaces that make up the board
     */
    public static Space [][] getSpaces() {
        return board;
    }
    /**
     * If a king is in check the inCheck flag is set
     * @param x boolean value to set inCheck flag too
     */
    public static void setInCheck(boolean x) {
        inCheck = x;
    }

    /**
     * Gets inCheck
     * @return boolean
     */
    public static boolean getInCheck() {
        return inCheck;
    }

    /**
     * Get list of moves that white is allowed to make when it is in check
     * @return List of Strings of legal moves
     */
    public static List<String> WgetGoodInCheckMoves() {
        return WgoodInCheckMoves;
    }

    /**
     * Add a safe move that white can make when it in check
     * @param goodInCheckMoves String
     */
    public static void WsetGoodInCheckMoves(String goodInCheckMoves) {
        Board.WgoodInCheckMoves.add(goodInCheckMoves);
    }

    /**
     * Get list of moves that black is allowed to make when it is in check
     * @return List of Strings of legal moves
     */
    public static List<String> BgetGoodInCheckMoves() {
        return BgoodInCheckMoves;
    }

    /**
     * Add a safe move that black can make when it in check
     * @param goodInCheckMoves String
     */
    public static void BsetGoodInCheckMoves(String goodInCheckMoves) {
        Board.BgoodInCheckMoves.add(goodInCheckMoves);
    }

    /**
     * get array of all the white pieces
     * @return array of white pieces
     */
    public static Piece [] getWhitePieces() {
        return whitePieces;
    }

    /**
     * get array of all the black pieces
     * @return array of black pieces
     */
    public static Piece [] getBlackPieces() {
        return blackPieces;
    }

    /**4
     * check if inCheck flag is set
     * @return boolean
     */
    public static boolean isCheckMate() {
        return checkMate;
    }
    /**
     * set the checkMate flag if check-mate occurs
     * @param checkMate boolean
     */
    public static void setCheckMate(boolean checkMate) {
        Board.checkMate = checkMate;
    }

    /**
     * Method to run through all of the pieces and recalculate all there possible moves
     * this occurs after every legal move. If any piece has a king in check this method will check
     * and set appropriate flags. It will also reset good in check moves to empty if the king is
     * no longer in check
     */
    public  static void reState() {
        //System.out.println(Arrays.toString(whitePieces));
        //System.out.println(Arrays.toString(blackPieces));
        for(int i = 0; i < whitePieces.length; i++) {
            whitePieces[i].calculateMoves();
            if(whitePieces[i].isiHaveCheck()) {
                Board.setInCheck(true);
            }
        }
        for(int i = 0; i < blackPieces.length; i++) {
            blackPieces[i].calculateMoves();
            if(blackPieces[i].isiHaveCheck()) {
                Board.setInCheck(true);
            }
        }

        if(!inCheck) {
            WgoodInCheckMoves.clear();
            BgoodInCheckMoves.clear();
        }
    }

    /**
     * Method for checking if a pawn has made it to its opposing side and what it should be premoted to
     * @param turn boolean for whose turn it is
     */
    public void checkPromotion(boolean turn) {
        Piece newPiece;
        if(turn) {
            for(int i = 0; i < 8; i++) {
                Space tmp = board[0][i];
                if(tmp.getPiece() instanceof Pawn) {
                    switch(promotion) {
                        case "R":
                            newPiece = new Rook("wR", tmp, x.getResources().getDrawable(R.drawable.wrook, x.getTheme()));
                            break;
                        case "N":
                            newPiece = new Knight("wN", tmp, x.getResources().getDrawable(R.drawable.wknight, x.getTheme()));
                            break;
                        case "B":
                            newPiece = new Bishop("wB", tmp, x.getResources().getDrawable(R.drawable. wbishop, x.getTheme()));
                            break;
                        case "Q":
                            newPiece = new Queen("wQ", tmp, x.getResources().getDrawable(R.drawable.wqueen, x.getTheme()));
                            break;
                        default:
                            newPiece = new Queen("wQ", tmp, x.getResources().getDrawable(R.drawable.wqueen, x.getTheme()));
                    }
                    replacePiece(turn, newPiece);
                    tmp.setPiece(newPiece);
                }
            }
            promotion = "Q";
        }
        else {
            for(int i = 0; i < 8; i++) {
                Space tmp = board[7][i];
                if(tmp.getPiece() instanceof Pawn) {
                    switch(promotion) {
                        case "R":
                            newPiece = new Rook("bR", tmp, x.getResources().getDrawable(R.drawable.brook, x.getTheme()));
                            break;
                        case "N":
                            newPiece = new Knight("bN", tmp, x.getResources().getDrawable(R.drawable.bknight, x.getTheme()));
                            break;
                        case "B":
                            newPiece = new Bishop("bB", tmp, x.getResources().getDrawable(R.drawable.bbishop, x.getTheme()));
                            break;
                        case "Q":
                            newPiece = new Queen("bQ", tmp, x.getResources().getDrawable(R.drawable.bqueen, x.getTheme()));
                            break;
                        default:
                            newPiece = new Queen("bQ", tmp, x.getResources().getDrawable(R.drawable.bqueen, x.getTheme()));
                    }
                    replacePiece(turn, newPiece);
                    tmp.setPiece(newPiece);
                }
            }
            promotion = "Q";
        }
    }

    /**
     * in the event of a pawn promotion the original pawn must be replaced with the new piece in
     * its respective list
     * @param turn boolean for whose turn it is
     * @param newPiece new piece to replace pawn with
     */
    public void replacePiece(boolean turn, Piece newPiece) {
        if(turn) {
            for(int i = 0; i < whitePieces.length; i++) {
                if(newPiece.equals(whitePieces[i])) {
                    whitePieces[i] = newPiece;
                }
            }
        }
        else {
            for(int i = 0; i < blackPieces.length; i++) {
                if(newPiece.equals(blackPieces[i])) {
                    blackPieces[i] = newPiece;
                }
            }
        }
    }

    /**
     * Method for converting the String name of a space to its index in the 2D array of spaces
     * @param name name of space wanted
     * @return space desired
     */
    public static Space fetchSpace(String name) {
        char c = name.charAt(0);
        int r = Character.getNumericValue(name.charAt(1));
        int col;
        int row;
        switch(c) {
            case 'a':
                col = 0;
                break;
            case 'b':
                col = 1;
                break;
            case 'c':
                col = 2;
                break;
            case 'd':
                col = 3;
                break;
            case 'e':
                col = 4;
                break;
            case 'f':
                col = 5;
                break;
            case 'g':
                col = 6;
                break;
            case 'h':
                col = 7;
                break;
            default:
                col = 0;
        }

        switch(r) {
            case 8:
                row = 0;
                break;
            case 7:
                row = 1;
                break;
            case 6:
                row = 2;
                break;
            case 5:
                row = 3;
                break;
            case 4:
                row = 4;
                break;
            case 3:
                row = 5;
                break;
            case 2:
                row = 6;
                break;
            case 1:
                row = 7;
                break;
            default:
                row = 0;
        }

        return board[row][col];
    }

    /**
     * prints the board after move and all pieces have calculated there new move set
     * checks flags to see if any extra information should be printed
     */


    public static void print() {
        int rows = 8;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println(rows);
            rows--;
        }
        for(char a = 'a'; a <= 'h'; a++) {
            System.out.print(" " + a + " ");
        }
        System.out.println("");
        System.out.println("");
        if(inCheck && !checkMate) {
            System.out.println("Check");
        }
        if(isCheckMate()) {
            String winner = ((whoseTurn % 2 == 0) ? "Black wins" : "White wins");
            System.out.println("Checkmate");
            System.out.println(winner);
        }
        else {
            System.out.print((whoseTurn % 2 == 0) ? "Whites move: " : "Blacks move: ");
        }
    }


    public static String aiMove(){
        Piece colorPieces[] = (whoseTurn % 2 == 0) ? getWhitePieces() : getBlackPieces();
        Random rand = new Random();
        if(inCheck){
            List<String> tmp = ((whoseTurn % 2 == 0) ? Board.WgoodInCheckMoves : Board.BgoodInCheckMoves);
            String inCheckMoves[] = tmp.toArray(new String[tmp.size()]);
            if(inCheckMoves.length == 1){
                return inCheckMoves[0];
            }
            int escape = rand.nextInt(inCheckMoves.length - 1);
            return inCheckMoves[escape];
        }
        boolean dosentHaveMoves = true;
        String move = " ";
        while(dosentHaveMoves) {
            int num = rand.nextInt(colorPieces.length - 1);
            Piece chosen = colorPieces[num];
            String moves[] = chosen.getMoves();
            if(moves.length == 0)
                continue;
            else
                dosentHaveMoves = false;
            //System.out.println(moves.length + " " + chosen);
            int chosenMove;
            if(moves.length == 1)
                chosenMove = 0;
            else
                chosenMove = rand.nextInt(moves.length - 1);
            String spot = moves[chosenMove];
            Space tmp = fetchSpace(spot);
            if(tmp.getPiece() != null && tmp.getPiece().getType().charAt(0) == chosen.getType().charAt(0)){
                dosentHaveMoves = true;
                continue;
            }
            move = chosen.getSpace().getName() + " " + spot;
        }
        return move;
    }

    public void backUp(){
        backupBlackPieces.clear();
        backUpWhitePieces.clear();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                secondBoard[i][j] = new Space(board[i][j]);
                if(board[i][j].getPiece() != null){
                    switch(board[i][j].getPiece().getType().charAt(1)){
                        case 'p':
                            Pawn tmpP = new Pawn((Pawn) board[i][j].getPiece());
                            secondBoard[i][j].setPiece(tmpP);
                            if ((board[i][j].getPiece().getType().charAt(0) == 'w')) {
                                backUpWhitePieces.add(tmpP);
                            }
                            else {
                                backupBlackPieces.add(tmpP);
                            }
                            break;
                        case 'R':
                            Rook tmpR = new Rook((Rook) board[i][j].getPiece());
                            secondBoard[i][j].setPiece(tmpR);
                            if ((board[i][j].getPiece().getType().charAt(0) == 'w')) {
                                backUpWhitePieces.add(tmpR);
                            }
                            else {
                                backupBlackPieces.add(tmpR);
                            }
                            break;
                        case 'N':
                            Knight tmpN = new Knight((Knight)board[i][j].getPiece());
                            secondBoard[i][j].setPiece(tmpN);
                            if ((board[i][j].getPiece().getType().charAt(0) == 'w')) {
                                backUpWhitePieces.add(tmpN);
                            }
                            else {
                                backupBlackPieces.add(tmpN);
                            }
                            break;
                        case 'B':
                            Bishop tmpB = new Bishop((Bishop) board[i][j].getPiece());
                            secondBoard[i][j].setPiece(tmpB);
                            if ((board[i][j].getPiece().getType().charAt(0) == 'w')) {
                                backUpWhitePieces.add(tmpB);
                            }
                            else {
                                backupBlackPieces.add(tmpB);
                            }
                            break;
                        case 'Q':
                            Queen tmpQ = new Queen((Queen) board[i][j].getPiece());
                            secondBoard[i][j].setPiece(tmpQ);
                            if ((board[i][j].getPiece().getType().charAt(0) == 'w')) {
                                backUpWhitePieces.add(tmpQ);
                            }
                            else {
                                backupBlackPieces.add(tmpQ);
                            }
                            break;
                        case 'K':
                            King tmpK = new King((King) board[i][j].getPiece());
                            secondBoard[i][j].setPiece(tmpK);
                            if ((board[i][j].getPiece().getType().charAt(0) == 'w')) {
                                backUpWhitePieces.add(tmpK);
                            }
                            else {
                                backupBlackPieces.add(tmpK);
                            }
                            break;
                        default:
                            System.out.println("Thats not good");
                            secondBoard[i][j].setPiece(null);
                            break;
                    }
                }
            }
        }
    }

    public static void undo(){
        board = secondBoard;
        secondBoard = new Space[8][8];
        //System.out.println("to string white " + backUpWhitePieces);
        //System.out.println("to string of black " + backupBlackPieces);
        whitePieces = backUpWhitePieces.toArray(new Piece[backUpWhitePieces.size()]);
        //System.out.println("new white pieces" + Arrays.toString(whitePieces));
        backUpWhitePieces = new ArrayList<Piece>();
        blackPieces = backupBlackPieces.toArray(new Piece[backupBlackPieces.size()]);
        //System.out.println("new black pieces" + Arrays.toString(blackPieces));
        backupBlackPieces = new ArrayList<Piece>();
        Board.setInCheck(false);
        Board.setCheckMate(false);
        reState();
        reState();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j].refresh();
            }
        }
        whoseTurn--;
        //print();
    }
}