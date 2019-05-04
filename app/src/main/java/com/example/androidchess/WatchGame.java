package com.example.androidchess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class WatchGame extends AppCompatActivity {
    private Button next;
    private Button prev;
    private Button back;
    private TextView tbox;
    private ImageView board[] = new ImageView[64];
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
                foward();
            }
        });

        prev = findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    }

    public void generateBoard(){
        board[0] = findViewById(R.id.imageView);
        for(int i = 1; i < 64; i++){
            String name = "imageView" + (i + 1);
            int resID = getResources().getIdentifier(name, "id", "com.example.androidchess");
            board[i] = findViewById(resID);
        }
    }

    public void foward(){
        ImageView tar = null;
        ImageView dest = null;
        String move = moves.get(counter);
        String [] inputs = move.split(" ");
        for(int i = 0; i < 64; i++){
            if(board[i].getTag().equals(inputs[0])){
                tar = board[i];
            }
        }

        for(int i = 0; i < 64; i++){
            if(board[i].getTag().equals(inputs[1])){
                dest = board[i];
            }
        }

        dest.setImageDrawable(tar.getDrawable());
        tar.setImageDrawable(null);
        counter++;
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

        board[63].setImageDrawable(getResources().getDrawable(R.drawable.wrook, getTheme()));
        board[62].setImageDrawable(getResources().getDrawable(R.drawable.wknight, getTheme()));
        board[61].setImageDrawable(getResources().getDrawable(R.drawable.wbishop, getTheme()));
        board[60].setImageDrawable(getResources().getDrawable(R.drawable.wqueen, getTheme()));
        board[59].setImageDrawable(getResources().getDrawable(R.drawable.wking, getTheme()));
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
