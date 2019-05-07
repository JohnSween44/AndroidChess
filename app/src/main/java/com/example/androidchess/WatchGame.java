package com.example.androidchess;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WatchGame extends AppCompatActivity {
    private Button next;
    private Button prev;
    private Button back;
    private TextView tbox;
    private ImageView board[] = new ImageView[64];
    private List<String[]> boards = new ArrayList<String[]>();
    private List<String> moves;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_game);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Bundle bundle = getIntent().getExtras();
        moves = bundle.getStringArrayList("moves");
        tbox = findViewById(R.id.feedBack);
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter == (moves.size() - 2)){
                    endGame();
                }
                else {
                    forwards();
                }
            }
        });

        prev = findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter == 0){
                    String msg = "No previous moves";
                    tbox.setText(msg);
                }
                else {
                    backwards();
                }
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WatchGame.this, Games.class));
            }
        });

        generateBoard();
        resetBoard();
        createBoards();
    }

    public void createBoards(){
        String startboard[] = new String[64];
        startboard[0] = "bR";
        startboard[1] = "bN";
        startboard[2] = "bB";
        startboard[3] = "bQ";
        startboard[4] = "bK";
        startboard[5] = "bB";
        startboard[6] = "bN";
        startboard[7] = "bR";
        startboard[8] = "bp";
        startboard[9] = "bp";
        startboard[10] = "bp";
        startboard[11] = "bp";
        startboard[12] = "bp";
        startboard[13] = "bp";
        startboard[14] = "bp";
        startboard[15] = "bp";

        startboard[63] = "wR";
        startboard[62] = "wN";
        startboard[61] = "wB";
        startboard[60] = "wK";
        startboard[59] = "wQ";
        startboard[58] = "wB";
        startboard[57] = "wN";
        startboard[56] = "wR";
        startboard[55] = "wp";
        startboard[54] = "wp";
        startboard[53] = "wp";
        startboard[52] = "wp";
        startboard[51] = "wp";
        startboard[50] = "wp";
        startboard[49] = "wp";
        startboard[48] = "wp";

        boards.add(startboard);

        for(int i = 0; i < (moves.size() - 1); i++){
            String[] newBoard = new String[64];
            for(int j = 0; j < 64; j++){
                newBoard[j] = boards.get(boards.size() - 1)[j];
            }
            String [] inputs = moves.get(i).split(" ");
            String [] nextInputs = null;
            if(i != (moves.size() - 2))
                 nextInputs =  moves.get(i + 1).split(" ");

            if(nextInputs != null && nextInputs.length == 3){
                continue;
            }
            else if(inputs.length == 3){
                String newPiece = (counter % 2 == 0 ? "w" : "b") + inputs[2];
                newBoard[getIndex(inputs[1])] = newPiece;
                newBoard[getIndex(inputs[0])] = null;
                boards.add(newBoard);
            }
            else if(moves.get(i).equals("draw")){

            }
            else if(moves.get(i).equals("resign")){

            }
            else {
                newBoard[getIndex(inputs[1])] = newBoard[getIndex(inputs[0])];
                newBoard[getIndex(inputs[0])] = null;
                boards.add(newBoard);
            }
        }
    }

    public int getIndex(String spot){
        char c = spot.charAt(0);
        int r = Character.getNumericValue(spot.charAt(1));
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
                row = 8;
                break;
            case 6:
                row = 16;
                break;
            case 5:
                row = 24;
                break;
            case 4:
                row = 32;
                break;
            case 3:
                row = 40;
                break;
            case 2:
                row = 48;
                break;
            case 1:
                row = 56;
                break;
            default:
                row = 0;
        }

        return row + col;
    }

    public void generateBoard(){
        board[0] = findViewById(R.id.imageView);
        for(int i = 1; i < 64; i++){
            String name = "imageView" + (i + 1);
            int resID = getResources().getIdentifier(name, "id", "com.example.androidchess");
            board[i] = findViewById(resID);
        }
    }

    public void forwards(){
        counter++;
        for (int i = 0; i < 64; i++) {
            board[i].setImageDrawable(getImages(boards.get(counter)[i]));
        }
        String text = ((counter % 2 == 0) ? "Whites move was " : "Blacks move was ") + moves.get(counter);
        tbox.setText(text);
    }

    public void backwards(){
        counter--;
        for(int i = 0; i < 64; i++){
            board[i].setImageDrawable(getImages(boards.get(counter)[i]));
        }
        String text = ((counter % 2 == 0) ? "Whites move was " : "Blacks move was ") + moves.get(counter);
        tbox.setText(text);
    }

    public void endGame(){
        AlertDialog.Builder mDi = new AlertDialog.Builder(WatchGame.this);
        mDi.setCancelable(false);
        mDi.setTitle(moves.get(moves.size() - 1));
        mDi.setMessage("Would you like to replay this game again?");
        mDi.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetBoard();
            }
        });
        mDi.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(WatchGame.this, Games.class));
            }
        });
        Dialog d = mDi.create();
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    public Drawable getImages(String piece){
        if(piece == null){
            return null;
        }

        switch(piece){
            case "bR":
                return getResources().getDrawable(R.drawable.brook, getTheme());
            case "bN":
                return getResources().getDrawable(R.drawable.bknight, getTheme());
            case "bB":
                return getResources().getDrawable(R.drawable.bbishop, getTheme());
            case "bQ":
                return getResources().getDrawable(R.drawable.bqueen, getTheme());
            case "bK":
                return getResources().getDrawable(R.drawable.bking, getTheme());
            case "bp":
                return getResources().getDrawable(R.drawable.bpawn, getTheme());
            case "wR":
                return getResources().getDrawable(R.drawable.wrook, getTheme());
            case "wN":
                return getResources().getDrawable(R.drawable.wknight, getTheme());
            case "wB":
                return getResources().getDrawable(R.drawable.wbishop, getTheme());
            case "wQ":
                return getResources().getDrawable(R.drawable.wqueen, getTheme());
            case "wK":
                return getResources().getDrawable(R.drawable.wking, getTheme());
            case "wp":
                return getResources().getDrawable(R.drawable.wpawn, getTheme());
        }
        return null;
    }

    public void resetBoard(){
        counter = 0;
        board[0].setImageDrawable(getResources().getDrawable(R.drawable.brook, getTheme()));
        board[1].setImageDrawable(getResources().getDrawable(R.drawable.bknight, getTheme()));
        board[2].setImageDrawable(getResources().getDrawable(R.drawable.bbishop, getTheme()));
        board[3].setImageDrawable(getResources().getDrawable(R.drawable.bqueen, getTheme()));
        board[4].setImageDrawable(getResources().getDrawable(R.drawable.bking, getTheme()));
        board[5].setImageDrawable(getResources().getDrawable(R.drawable.bbishop, getTheme()));
        board[6].setImageDrawable(getResources().getDrawable(R.drawable.bknight, getTheme()));
        board[7].setImageDrawable(getResources().getDrawable(R.drawable.brook, getTheme()));
        board[8].setImageDrawable(getResources().getDrawable(R.drawable.bpawn, getTheme()));
        board[9].setImageDrawable(getResources().getDrawable(R.drawable.bpawn, getTheme()));
        board[10].setImageDrawable(getResources().getDrawable(R.drawable.bpawn, getTheme()));
        board[11].setImageDrawable(getResources().getDrawable(R.drawable.bpawn, getTheme()));
        board[12].setImageDrawable(getResources().getDrawable(R.drawable.bpawn, getTheme()));
        board[13].setImageDrawable(getResources().getDrawable(R.drawable.bpawn, getTheme()));
        board[14].setImageDrawable(getResources().getDrawable(R.drawable.bpawn, getTheme()));
        board[15].setImageDrawable(getResources().getDrawable(R.drawable.bpawn, getTheme()));

        for(int i = 16; i < 48; i++){
            board[i].setImageDrawable(null);
        }

        board[63].setImageDrawable(getResources().getDrawable(R.drawable.wrook, getTheme()));
        board[62].setImageDrawable(getResources().getDrawable(R.drawable.wknight, getTheme()));
        board[61].setImageDrawable(getResources().getDrawable(R.drawable.wbishop, getTheme()));
        board[60].setImageDrawable(getResources().getDrawable(R.drawable.wking, getTheme()));
        board[59].setImageDrawable(getResources().getDrawable(R.drawable.wqueen, getTheme()));
        board[58].setImageDrawable(getResources().getDrawable(R.drawable.wbishop, getTheme()));
        board[57].setImageDrawable(getResources().getDrawable(R.drawable.wknight, getTheme()));
        board[56].setImageDrawable(getResources().getDrawable(R.drawable.wrook, getTheme()));
        board[55].setImageDrawable(getResources().getDrawable(R.drawable.wpawn, getTheme()));
        board[54].setImageDrawable(getResources().getDrawable(R.drawable.wpawn, getTheme()));
        board[53].setImageDrawable(getResources().getDrawable(R.drawable.wpawn, getTheme()));
        board[52].setImageDrawable(getResources().getDrawable(R.drawable.wpawn, getTheme()));
        board[51].setImageDrawable(getResources().getDrawable(R.drawable.wpawn, getTheme()));
        board[50].setImageDrawable(getResources().getDrawable(R.drawable.wpawn, getTheme()));
        board[49].setImageDrawable(getResources().getDrawable(R.drawable.wpawn, getTheme()));
        board[48].setImageDrawable(getResources().getDrawable(R.drawable.wpawn, getTheme()));
    }
}
