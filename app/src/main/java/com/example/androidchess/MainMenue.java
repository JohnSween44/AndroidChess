package com.example.androidchess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenue extends AppCompatActivity{
    private Button play;
    private Button oldGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menue);
        play = findViewById(R.id.play);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenue.this, MainActivity.class));
            }
        });
        oldGames = findViewById(R.id.oldGames);
        oldGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenue.this, Games.class));
            }
        });
    }
}
