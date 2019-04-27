package com.example.androidchess;

import java.util.*;

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
    private static Piece [] whitePieces = new Piece[16];
    private static Piece [] blackPieces = new Piece[16];
    private static String promotion = "Q";
    private static List<String> WgoodInCheckMoves = new ArrayList<String>();
    private static List<String> BgoodInCheckMoves = new ArrayList<String>();
    private static boolean inCheck = false;
    private static boolean checkMate = false;
    private boolean drawFlag = false;
    private int whoseTurn = 0;
    private Scanner keys = new Scanner(System.in);

    /**
     * Constructor builds board and creates pieces
     */
    public Board() {
        int whiteIndex = 0;
        int blackIndex = 0;
        int colorPicker = 0;
        int row = 8;
        for(int i = 0; i < 8; i++) {
            char column = 'a';
            for(int j = 0; j < 8; j++) {
                String name = column + Integer.toString(row);
                String color = ((colorPicker % 2 == 0) ? "  " : "##");
                Space s = new Space(color, name);
                board[i][j] = s;
                column++;
                colorPicker++;
            }
            colorPicker--;
            row--;
        }

        for(int i = 0; i < 8; i++) {
            Pawn blackP = new Pawn("bp", board[1][i]);
            board[1][i].setPiece(blackP);
            blackPieces[blackIndex++] = blackP;
            Pawn whiteP = new Pawn("wp", board[6][i]);
            board[6][i].setPiece(whiteP);
            whitePieces[whiteIndex++] = whiteP;
        }

        Rook bRook1 = new Rook("bR", board[0][0]);
        Rook bRook2 = new Rook("bR", board[0][7]);
        board[0][0].setPiece(bRook1);
        blackPieces[blackIndex++] = bRook1;
        board[0][7].setPiece(bRook2);
        blackPieces[blackIndex++] = bRook2;
        Rook wRook1 = new Rook("wR", board[7][0]);
        Rook wRook2 = new Rook("wR", board[7][7]);
        board[7][0].setPiece(wRook1);
        whitePieces[whiteIndex++] = wRook1;
        board[7][7].setPiece(wRook2);
        whitePieces[whiteIndex++] = wRook2;
        Knight bKnight1 = new Knight("bN", board[0][1]);
        Knight bKnight2 = new Knight("bN", board[0][6]);
        board[0][1].setPiece(bKnight1);
        blackPieces[blackIndex++] = bKnight1;
        board[0][6].setPiece(bKnight2);
        blackPieces[blackIndex++] = bKnight2;
        Knight wKnight1 = new Knight("wN", board[7][1]);
        Knight wKnight2 = new Knight("wN", board[7][6]);
        board[7][1].setPiece(wKnight1);
        whitePieces[whiteIndex++] = wKnight1;
        board[7][6].setPiece(wKnight2);
        whitePieces[whiteIndex++] = wKnight2;
        Bishop bBishop1 = new Bishop("bB", board[0][2]);
        Bishop bBishop2 = new Bishop("bB", board[0][5]);
        board[0][2].setPiece(bBishop1);
        blackPieces[blackIndex++] = bBishop1;
        board[0][5].setPiece(bBishop2);
        blackPieces[blackIndex++] = bBishop2;
        Bishop wBishop1 = new Bishop("wB", board[7][2]);
        Bishop wBishop2 = new Bishop("wB", board[7][5]);
        board[7][2].setPiece(wBishop1);
        whitePieces[whiteIndex++] = wBishop1;
        board[7][5].setPiece(wBishop2);
        whitePieces[whiteIndex++] = wBishop2;
        Queen bQueen = new Queen("bQ", board[0][3]);
        Queen wQueen = new Queen("wQ", board[7][3]);
        board[0][3].setPiece(bQueen);
        blackPieces[blackIndex++] = bQueen;
        board[7][3].setPiece(wQueen);
        whitePieces[whiteIndex++] = wQueen;
        King bKing = new King("bK", board[0][4]);
        King wKing = new King("wK", board[7][4]);
        board[0][4].setPiece(bKing);
        blackPieces[blackIndex++] = bKing;
        board[7][4].setPiece(wKing);
        whitePieces[whiteIndex++] = wKing;
        reState();
        print();
    }
    /**
     * Starts the game/move by parsing user input to determine if there castle-ing,
     * promoting, drawing, resigning, and if the move is legal and can be cared out. Also determining if the
     * the game has ended
     * @return Returns boolean true always until there is a winner
     * 		   in which case it returns false as the game has now ended
     */
    public boolean play() {
        Piece inDangerPiece = null;
        boolean possibleCapture = false;
        String input = keys.nextLine();
        System.out.println("");
        String [] inputs = input.split(" ");

        if(inputs.length > 3 || inputs.length < 1) {
            System.out.print("Illegal move, try again: ");
            return true;
        }

        if(inputs.length == 1) {
            if(inputs[0].equals("resign")) {
                String winner = ((whoseTurn % 2 == 0) ? "Black wins" : "White wins");
                System.out.println(winner);
                return false;
            }
            else if(inputs[0].equals("draw") && drawFlag) {
                return false;
            }
            else {
                System.out.print("Illegal move, try again: ");
                return true;
            }
        }

        String target = inputs[0];
        String destination = inputs[1];

        if(target.equals(destination)) {
            System.out.print("Illegal move, try again: ");
            return true;
        }
        if(target.charAt(0) < 'a' || target.charAt(0) > 'h' || target.charAt(1) > '8' || target.charAt(1) < '1') {
            System.out.print("Illegal move, try again: ");
            return true;
        }
        if(destination.charAt(0) < 'a' || destination.charAt(0) > 'h' || destination.charAt(1) > '8' || destination.charAt(1) < '1') {
            System.out.print("Illegal move, try again: ");
            return true;
        }

        Space tar = Board.fetchSpace(target);
        Space dest = Board.fetchSpace(destination);

        if(tar.getPiece() == null) {
            System.out.print("Illegal move, try again: ");
            return true;
        }
        if((tar.getPiece().getType().charAt(0) == 'b' && whoseTurn % 2 == 0) || (tar.getPiece().getType().charAt(0) == 'w' && whoseTurn % 2 != 0)) {
            System.out.print("Illegal move, try again: ");
            return true;
        }

        if(tar.getPiece() != null && dest.getPiece() != null) {
            if(tar.getPiece().getType().charAt(0) == dest.getPiece().getType().charAt(0)) {
                System.out.print("Illegal move, try again: ");
                return true;
            }
        }

        if((whoseTurn % 2 == 0) ? ((tar.getName().equals("e1") && dest.getName().equals("g1")) || (tar.getName().equals("e1") && dest.getName().equals("c1"))) :
                ((tar.getName().equals("e8") && dest.getName().equals("g8")) || (tar.getName().equals("e8") && dest.getName().equals("c8")))) {

            if(inCheck) {
                System.out.print("Illegal move, try again: ");
                return true;
            }
            if(tar.getPiece() == null || tar.getPiece().getHasItMovedYet()) {
                System.out.print("Illegal move, try again: ");
                return true;
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
                System.out.print("Illegal move, try again: ");
                return true;
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
                System.out.print("Illegal move, try again: ");
                return true;
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
                            System.out.print("Illegal move, try again: ");
                            return true;
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
            print();
            if(isCheckMate())
                return false;

            return true;

        }

        if(inputs.length == 3) {
            String thirdOption = inputs[2];
            if(thirdOption.equals("draw?")) {
                drawFlag = true;
            }
            else if(tar.getPiece() instanceof Pawn &&
                    thirdOption.equals("R") || thirdOption.equals("N")  ||
                    thirdOption.equals("B") || thirdOption.equals("Q")) {

                if(tar.getPiece().getType().charAt(0) == 'b' && dest.getName().charAt(1) == '1') {
                    promotion = thirdOption;
                }
                else if(tar.getPiece().getType().charAt(0) == 'w' && dest.getName().charAt(1) == '8') {
                    promotion = thirdOption;
                }
                else {
                    System.out.print("Illegal move, try again: ");
                    return true;
                }

            }
            else {
                System.out.print("Illegal move, try again: ");
                return true;
            }

        }
        if(dest.getPiece() != null) {
            inDangerPiece = dest.getPiece();
            possibleCapture = true;
        }
        if(inCheck) {
            if((whoseTurn % 2 == 0) ? Board.WgoodInCheckMoves.size() == 0 : Board.BgoodInCheckMoves.size() == 0) {
                setCheckMate(true);
                print();
                if(isCheckMate())
                    return false;
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
                    print();
                    if(isCheckMate())
                        return false;

                    return true;
                }
                else {
                    String [] m = tar.getPiece().getMoves();
                    System.out.println(Arrays.toString(m));
                    System.out.print("Illegal move, try again: ");
                    return true;
                }
            }
            else {
                System.out.print("Illegal move, try again: ");
                return true;
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
            print();
            if(isCheckMate())
                return false;

            return true;
        }
        else {
            String [] m = tar.getPiece().getMoves();
            System.out.print("Illegal move, try again: ");
            return true;
        }
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
    public void reState() {
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
                            newPiece = new Rook("wR", tmp);
                            break;
                        case "N":
                            newPiece = new Knight("wN", tmp);
                            break;
                        case "B":
                            newPiece = new Bishop("wB", tmp);
                            break;
                        case "Q":
                            newPiece = new Queen("wQ", tmp);
                            break;
                        default:
                            newPiece = new Queen("wQ", tmp);
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
                            newPiece = new Rook("bR", tmp);
                            break;
                        case "N":
                            newPiece = new Knight("bN", tmp);
                            break;
                        case "B":
                            newPiece = new Bishop("bB", tmp);
                            break;
                        case "Q":
                            newPiece = new Queen("bQ", tmp);
                            break;
                        default:
                            newPiece = new Queen("bQ", tmp);
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
    public void print() {
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
}