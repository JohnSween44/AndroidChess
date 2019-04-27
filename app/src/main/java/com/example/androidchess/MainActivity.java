package com.example.androidchess;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public ImageView board[] = new ImageView[64];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        generateBoard();
        Board logicBoard = new Board(MainActivity.this);
    }
    private void generateBoard() {
        int index = 0;
        int row = 8;
        for(int i = 0; i < 8; i++) {
            char column = 'a';
            for(int j = 0; j < 8; j++) {
                String name = column + Integer.toString(row);
                int resID = getResources().getIdentifier(name, "Id", "com.example.androidchess");
                board[index++] = findViewById(resID);
                column++;
            }
            row--;
        }
    }
}
