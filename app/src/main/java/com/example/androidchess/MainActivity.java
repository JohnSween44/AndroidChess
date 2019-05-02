package com.example.androidchess;

import android.app.AlertDialog;
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
    private boolean undoFlag = true;
    private List <String> savedMoves = new ArrayList<String>();


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
        int result;
        switch(view.getId()){
            case R.id.Quit:
                quit();
                break;
            case R.id.Undo:
                if(undoFlag) {
                    //System.out.println("undo called");
                    logicBoard.undo();
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
                result = logicBoard.play(test);
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
                result = logicBoard.play("draw");
                savedMoves.add("draw");
                handleResult(result);
                break;
            case R.id.Resign:
                result = logicBoard.play("resign");
                savedMoves.add("resign");
                handleResult(result);
                break;
            default:
                if(firstSelection){
                    view.setBackgroundColor(Color.parseColor("#A82746E4"));
                    destination = view.getTag().toString();
                    String move = target + " " + destination;
                    result = logicBoard.play(move);
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
                        if(gameName.getText().equals("")){
                            x.setMessage("Please enter a name for this game");
                            x.show();
                        }
                        else{
                            SavedGame newGame = new SavedGame(gameName.getText().toString(), savedMoves);
                        }
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
