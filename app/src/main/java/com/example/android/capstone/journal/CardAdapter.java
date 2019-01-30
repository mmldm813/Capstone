package com.example.android.capstone.journal;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.capstone.R;
import com.example.android.capstone.database.AppDatabase;
import com.example.android.capstone.database.AppExecutors;
import com.example.android.capstone.database.JournalDao;

import java.sql.Date;
import java.util.List;

import timber.log.Timber;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Activity activity;
    private List<java.sql.Date> dates;
    private AppDatabase db;

    public class ViewHolder extends RecyclerView.ViewHolder {
        Date date;
        TextView dateText;
        LinearLayout journalRowContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.date_text);
            journalRowContainer = itemView.findViewById(R.id.jounrnal_exercise_container);
        }
    }

    public void setData(List<java.sql.Date> dates) {
        if (this.dates != null) {
            this.dates.clear();
            this.dates.addAll(dates);
        } else {
            this.dates = dates;
        }
        notifyDataSetChanged();
    }

    public CardAdapter(Activity activity) {
        this.activity = activity;
         db = AppDatabase.getInstance(activity.getApplicationContext());
    }

    @Override
    public int getItemCount() {
        return dates == null ? 0 : dates.size();
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
    public void onBindViewHolder(@NonNull final CardAdapter.ViewHolder viewHolder, int position) {
        final Date date = dates.get(position);

        viewHolder.date = date;
        viewHolder.dateText.setText(date.toString());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<JournalDao.JournalExercise> journalExercises =
                        db.journalDao().loadJournalEntryAndExerciseByDate(date);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewHolder.date == date) {
                            Timber.e("size=" + journalExercises.size());
                            for (JournalDao.JournalExercise journalExercise : journalExercises) {
                                View journalRow = LayoutInflater.from(activity)
                                        .inflate(R.layout.journal_card_row, null, false);
                                ((TextView) journalRow.findViewById(R.id.exercise_name))
                                        .setText(journalExercise.name);
                                String duration;
                                if (journalExercise.totalDuration == 0) {
                                    duration = "not attempted";
                                } else {
                                    duration = String.format("%02d:%02d",
                                            journalExercise.totalDuration / 60,
                                            journalExercise.totalDuration % 60);
                                }
                                ((TextView) journalRow.findViewById(R.id.duration))
                                        .setText(duration);
                                viewHolder.journalRowContainer.addView(journalRow);

                            }
                        }
                    }
                });
            }
        });
    }
}
