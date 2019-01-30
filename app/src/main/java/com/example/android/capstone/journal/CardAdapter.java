package com.example.android.capstone.journal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.capstone.R;
import com.example.android.capstone.database.JournalDao;

import java.sql.Date;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<JournalDao.JournalExercise> journalExercises;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.date_text);
        }
    }

    public void setData(List<JournalDao.JournalExercise> journalExercises) {
        if (this.journalExercises != null) {
            this.journalExercises.clear();
            this.journalExercises.addAll(journalExercises);
        } else {
            this.journalExercises = journalExercises;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return journalExercises == null ? 0: 1;
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder viewHolder, int position) {
        final Date date = journalExercises.get(position).date;

        viewHolder.dateText.setText(date.toString());
    }
}
