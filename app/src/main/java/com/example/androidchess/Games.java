package com.example.androidchess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Games extends AppCompatActivity {
    private List<SavedGame> games = new ArrayList<SavedGame>();
    private RecyclerView gamesList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton back;
    private ImageButton sort;
    private String gameFile = "gameSaves.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        gamesList = findViewById(R.id.gamesList);

        try {
            FileInputStream in = openFileInput(gameFile);
            boolean cont = true;
            ObjectInputStream input = new ObjectInputStream(in);
            while(cont){
                Object obj = input.readObject();
                if(obj != null)
                    games.add((SavedGame)obj);
                else
                    cont = false;
            }
        }
        catch(Exception e){
            System.out.println("games " + e);
        }
        Collections.sort(games, new SortGamesByName());
        gamesList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        gamesList.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(games, this);
        gamesList.setAdapter(mAdapter);

        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Games.this, MainMenue.class));
            }
        });
        sort = findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(Games.this, sort);
                pop.getMenuInflater().inflate(R.menu.menu_games, pop.getMenu());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.sortName){
                            Collections.sort(games, new SortGamesByName());
                            mAdapter.notifyDataSetChanged();
                            return true;
                        }
                        else if(item.getItemId() == R.id.sortDate){
                            Collections.sort(games, new SortGamesByDate());
                            mAdapter.notifyDataSetChanged();
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                });
                pop.show();
            }
        });
    }

    public void rewrite(){
        if(games.size() == 0){
            try {
                FileOutputStream file = openFileOutput(gameFile, MODE_PRIVATE);
                String empty = "";
                file.write(empty.getBytes());
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        for(int i = 0; i < games.size(); i++){
            if(i == 0){
                try {
                    FileOutputStream file = openFileOutput(gameFile, MODE_PRIVATE);
                    ObjectOutputStream fil = new ObjectOutputStream(file);
                    fil.writeObject(games.get(i));
                    fil.close();
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
            else{
                try {
                    FileOutputStream file = openFileOutput(gameFile, MODE_APPEND);
                    AppendingObjectOutputStream fil = new AppendingObjectOutputStream(file);
                    fil.writeObject(games.get(i));
                    fil.close();
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }

    @Override
    public void onPause(){
        rewrite();
        super.onPause();
    }

    @Override
    public void onStop(){
        rewrite();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        rewrite();
        super.onDestroy();
    }
}
