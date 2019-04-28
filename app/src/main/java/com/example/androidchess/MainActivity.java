package com.example.androidchess;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView board[] = new ImageView[64];
    private Board logicBoard;
    private String target;
    private View targetView;
    private String destination;
    private boolean firstSelection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        generateBoard();
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
        if(firstSelection){
            view.setBackgroundColor(Color.parseColor("#A82746E4"));
            destination = view.getTag().toString();
            String move = target + " " + destination;
            int resutlt = logicBoard.play(move);
            view.setBackgroundColor(Color.parseColor("#00000000"));
            targetView.setBackgroundColor(Color.parseColor("#00000000"));
            firstSelection = false;
            target = null;
            destination = null;
            targetView = null;
        }
        else{
            view.setBackgroundColor(Color.parseColor("#A82746E4"));
            targetView = view;
            target = view.getTag().toString();
            firstSelection = true;
        }
    }

}
