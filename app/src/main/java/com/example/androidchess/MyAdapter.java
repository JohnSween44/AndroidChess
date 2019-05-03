package com.example.androidchess;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<SavedGame> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView gameName;
        public TextView gameDate;
        public TextView gameMoves;
        public View div;
        public ConstraintLayout list;
        public Button play;
        public Button delete;

        public MyViewHolder(View view) {
            super(view);
            gameName = view.findViewById(R.id.gameName);
            gameDate = view.findViewById(R.id.dateTime);
            gameMoves = view.findViewById(R.id.numMoves);
            list = view.findViewById(R.id.list);
            div = view.findViewById(R.id.div);
            play = view.findViewById(R.id.viewGame);
            delete = view.findViewById(R.id.remove);
        }
    }

    public MyAdapter(List<SavedGame> games) {
        mDataset = games;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        SavedGame tmp = mDataset.get(i);
        myViewHolder.gameName.setText(tmp.getGameName());
        myViewHolder.gameDate.setText(tmp.getDate());
        int num = tmp.getMoves().size();
        String mov = num + " moves";
        myViewHolder.gameMoves.setText(mov);
        myViewHolder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataset.remove(mDataset.get(i));
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mDataset.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
