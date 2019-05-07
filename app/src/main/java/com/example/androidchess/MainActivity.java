package com.example.androidchess;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView board[] = new ImageView[64];
    private Board logicBoard;
    private String target;
    private View targetView;
    private String destination;
    private boolean firstSelection = false;
    private Button draw;
    private Button resign;
    private Button undo;
    private Button aimove;
    private Button quit;
    private TextView msgBox;
    private boolean itsHasntEnded = true;
    private boolean undoFlag = false;
    private List <String> savedMoves = new ArrayList<String>();
    private int lastResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        draw = findViewById(R.id.Draw);
        draw.setOnClickListener(this);
        resign = findViewById(R.id.Resign);
        resign.setOnClickListener(this);
        undo = findViewById(R.id.Undo);
        undo.setOnClickListener(this);
        aimove = findViewById(R.id.aimove);
        aimove.setOnClickListener(this);
        quit = findViewById(R.id.Quit);
        quit.setOnClickListener(this);
        msgBox = findViewById(R.id.messageBox);
        generateBoard();
    }

    @Override
    protected void onStart(){
        super.onStart();
        logicBoard = new Board(MainActivity.this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        logicBoard = new Board(MainActivity.this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        logicBoard = null;
    }

    @Override
    protected void onStop(){
        super.onStop();
        logicBoard = null;
    }

    private void generateBoard() {
        int index = 0;
        int row = 8;
        for(int i = 0; i < 8; i++) {
            char column = 'a';
            for(int j = 0; j < 8; j++) {
                String name = column + Integer.toString(row);
                int resID = getResources().getIdentifier(name, "id", "com.example.androidchess");
                board[index] = findViewById(resID);
                board[index++].setOnClickListener(this);
                column++;
            }
            row--;
        }
    }

    @Override
    public void onClick(View view){
        final int result;
        switch(view.getId()){
            case R.id.Quit:
                quit();
                break;
            case R.id.Undo:
                if(undoFlag && lastResult != -1) {
                    //System.out.println("undo called");
                    logicBoard.undo();
                    String tmp[] = savedMoves.get(savedMoves.size() - 1).split(" ");
                    if(tmp.length == 3){
                        savedMoves.remove(savedMoves.size() - 1);
                        savedMoves.remove(savedMoves.size() - 1);
                    }
                    else {
                        savedMoves.remove(savedMoves.size() - 1);
                    }
                    undoFlag = false;
                    if (itsHasntEnded) {
                        if ((logicBoard.getWhoseTurn() % 2 == 0)) {
                            if (logicBoard.getInCheck()) {
                                msgBox.setText(R.string.whitesInCheck);
                            } else {
                                msgBox.setText(R.string.whitesTurn);
                            }
                        } else {
                            if (logicBoard.getInCheck()) {
                                msgBox.setText(R.string.blacksInCheck);
                            } else {
                                msgBox.setText(R.string.blacksTurn);
                            }
                        }
                    }
                }
                else{
                    msgBox.setText(R.string.cantUndo);
                }
                break;
            case R.id.aimove:
                String test = logicBoard.aiMove();
                String [] inputs = test.split(" ");
                result = logicBoard.play(promotionCheck(inputs[0], inputs[1]));
                lastResult = result;
                undoFlag = true;
                savedMoves.add(test);
                handleResult(result);
                //System.out.println(result + " " + test);
                //logicBoard.print();
                if(itsHasntEnded){
                    if ((logicBoard.getWhoseTurn() % 2 == 0)) {
                        if(logicBoard.getInCheck()){
                            msgBox.setText(R.string.whitesInCheck);
                        }
                        else{
                            msgBox.setText(R.string.whitesTurn);
                        }
                    }
                    else {
                        if(logicBoard.getInCheck()){
                            msgBox.setText(R.string.blacksInCheck);
                        }
                        else{
                            msgBox.setText(R.string.blacksTurn);
                        }
                    }
                }
                undoFlag = true;
                break;
            case R.id.Draw:
                savedMoves.add("draw");
                AlertDialog.Builder draw = new AlertDialog.Builder(MainActivity.this);
                draw.setCancelable(false);
                draw.setTitle(R.string.drawquest);
                draw.setMessage((logicBoard.getWhoseTurn() % 2 == 0) ? R.string.propositionWhite : R.string.propositionBlack);
                draw.setPositiveButton(R.string.draw, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savedMoves.add("draw");
                        handleResult(2);
                    }
                });
                draw.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                Dialog dialog = draw.create();
                dialog.show();
                break;
            case R.id.Resign:
                result = logicBoard.play("resign");
                lastResult = result;
                savedMoves.add("resign");
                handleResult(result);
                break;
            default:
                if(firstSelection){
                    view.setBackgroundColor(Color.parseColor("#A82746E4"));
                    destination = view.getTag().toString();
                    String move = promotionCheck(target, destination);
                    result = logicBoard.play(move);
                    lastResult = result;
                    if(result != -1)
                        savedMoves.add(move);
                    handleResult(result);
                    view.setBackgroundColor(Color.parseColor("#00000000"));
                    targetView.setBackgroundColor(Color.parseColor("#00000000"));
                    firstSelection = false;
                    target = null;
                    destination = null;
                    targetView = null;
                    undoFlag = true;
                    if(itsHasntEnded){
                        if ((logicBoard.getWhoseTurn() % 2 == 0)) {
                            if(logicBoard.getInCheck()){
                                msgBox.setText(R.string.whitesInCheck);
                            }
                            else{
                                msgBox.setText(R.string.whitesTurn);
                            }
                        }
                        else {
                            if(logicBoard.getInCheck()){
                                msgBox.setText(R.string.blacksInCheck);
                            }
                            else{
                                msgBox.setText(R.string.blacksTurn);
                            }
                        }
                    }
                }
                else{
                    view.setBackgroundColor(Color.parseColor("#A82746E4"));
                    targetView = view;
                    target = view.getTag().toString();
                    firstSelection = true;
                }
                break;
        }

    }

    public String promotionCheck(final String tar, final String dest){
        Space t = logicBoard.fetchSpace(tar);
        Space d = logicBoard.fetchSpace(dest);
        Boolean flag = false;
        final String ret[] ={null};
        ret[0] = tar + " " + dest;
        if(t.getPiece() == null){
            return ret[0];
        }
        if((t.getPiece().getType().equals("wp") && d.getName().charAt(1) == '8') ||
                t.getPiece().getType().equals("bp") && d.getName().charAt(1) == '1'){
            final AlertDialog.Builder promote = new AlertDialog.Builder(MainActivity.this);
            String title = "Promote a Pawn!";
            promote.setTitle(title);
            String options[] = {"Queen","Bishop","Knight","Rook"};
            promote.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            String move = tar + " " + dest +" Q";
                            logicBoard.undo();
                            int r = logicBoard.play(move);
                            savedMoves.add(move);
                            handleResult(r);
                            break;
                        case 1:
                            String move1 = tar + " " + dest +" B";
                            logicBoard.undo();
                            int r1 = logicBoard.play(move1);
                            savedMoves.add(move1);
                            handleResult(r1);
                            break;
                        case 2:
                            String move2 = tar + " " + dest +" N";
                            logicBoard.undo();
                            int r2 = logicBoard.play(move2);
                            savedMoves.add(move2);
                            handleResult(r2);
                            break;
                        case 3:
                            String move3 = tar + " " + dest +" R";
                            logicBoard.undo();
                            int r3 = logicBoard.play(move3);
                            savedMoves.add(move3);
                            handleResult(r3);
                            break;
                    }
                }
            });
            promote.setCancelable(false);
            AlertDialog dialog = promote.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        return tar + " " + dest;
    }

    public void handleResult(int result){
        switch(result) {
            case -1:
                msgBox.setText(R.string.error);
                break;
            case 0:
                if ((logicBoard.getWhoseTurn() % 2 == 0)) {
                    msgBox.setText(R.string.checkMateBlack);
                }
                else {
                    msgBox.setText(R.string.checkMateWhite);
                }
                itsHasntEnded = false;
                endGame();
                break;
            case 1:
                if ((logicBoard.getWhoseTurn() % 2 == 0)) {
                    msgBox.setText(R.string.whiteResigns);
                }
                else {
                    msgBox.setText(R.string.blackResigns);
                }
                endGame();
                break;
            case 2:
                msgBox.setText(R.string.draw);
                endGame();
                break;
            case 4:
                if ((logicBoard.getWhoseTurn() % 2 == 0)) {
                    msgBox.setText(R.string.propositionWhite);
                }
                else {
                    msgBox.setText(R.string.propositionBlack);
                }
                break;
            case 3:
                break;
            default:
                msgBox.setText(R.string.error);
                break;
        }
    }

    public void endGame(){
        final EditText gameName = new EditText(this);
        gameName.setInputType(InputType.TYPE_CLASS_TEXT );
        gameName.setHint(R.string.enterName);
        //gameName.setLayout
        final AlertDialog.Builder x = new AlertDialog.Builder(MainActivity.this);
                x.setTitle(msgBox.getText());
                x.setMessage("Would you like to save this game?");
                x.setView(gameName);
                x.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SavedGame newGame = new SavedGame(gameName.getText().toString(), savedMoves, msgBox.getText().toString());
                        try{
                            String fiName = "gameSaves.txt";
                            File fi = new File(getFilesDir(), fiName);
                            if(fi.exists() && fi.length()!= 0){
                                FileOutputStream file = openFileOutput(fiName, MODE_APPEND);
                                AppendingObjectOutputStream fil = new AppendingObjectOutputStream(file);
                                fil.writeObject(newGame);
                                fil.close();
                            }
                            else{
                                FileOutputStream file = openFileOutput(fiName, MODE_PRIVATE);
                                ObjectOutputStream fil = new ObjectOutputStream(file);
                                fil.writeObject(newGame);
                                fil.close();
                            }

                        }
                        catch (Exception w){
                            System.out.println(w);
                        }
                        startActivity(new Intent(MainActivity.this, Games.class));
                    }
                });
                x.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, MainMenue.class));
                    }
                });
                x.show();
    }

    public void quit(){
        AlertDialog.Builder confirm = new AlertDialog.Builder(MainActivity.this);
        confirm.setTitle("Quit?");
        confirm.setMessage("Are you sure you want to quit this game");
        confirm.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, MainMenue.class));
            }
        });
        confirm.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        confirm.show();
    }
}
