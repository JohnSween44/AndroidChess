package com.example.androidchess;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView msgBox;
    private boolean itsHasntEnded = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        generateBoard();
        draw = findViewById(R.id.Draw);
        draw.setOnClickListener(this);
        resign = findViewById(R.id.Resign);
        resign.setOnClickListener(this);
        undo = findViewById(R.id.Undo);
        undo.setOnClickListener(this);
        aimove = findViewById(R.id.aimove);
        aimove.setOnClickListener(this);
        msgBox = findViewById(R.id.messageBox);
        logicBoard = new Board(MainActivity.this);
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
            case R.id.Undo:
                //nothing yet
                break;
            case R.id.aimove:
                //nothing yet
                break;
            case R.id.Draw:
                result = logicBoard.play("draw");
                handleResult(result);
                break;
            case R.id.Resign:
                result = logicBoard.play("resign");
                handleResult(result);
                break;
            default:
                if(firstSelection){
                    view.setBackgroundColor(Color.parseColor("#A82746E4"));
                    destination = view.getTag().toString();
                    String move = target + " " + destination;
                    result = logicBoard.play(move);
                    handleResult(result);
                    view.setBackgroundColor(Color.parseColor("#00000000"));
                    targetView.setBackgroundColor(Color.parseColor("#00000000"));
                    firstSelection = false;
                    target = null;
                    destination = null;
                    targetView = null;
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
                //add end game method
                break;
            case 1:
                if ((logicBoard.getWhoseTurn() % 2 == 0)) {
                    msgBox.setText(R.string.whiteResigns);
                }
                else {
                    msgBox.setText(R.string.blackResigns);
                }
                //add end game method
                break;
            case 2:
                msgBox.setText(R.string.draw);
                //add end game method
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

}
