package com.example.android.capstone.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.capstone.MyApplication;
import com.example.android.capstone.R;
import com.example.android.capstone.data.Exercise;
import com.example.android.capstone.database.AppDatabase;
import com.example.android.capstone.database.AppExecutors;
import com.example.android.capstone.database.JournalDao;

import java.sql.Date;
import java.util.List;

class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private int appWidgetId;
    private MyApplication myApplication;
    private List<JournalDao.JournalExercise> journalExercises;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        myApplication = ((MyApplication) context.getApplicationContext());
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        // not used
    }

    @Override
    public void onDataSetChanged() {
        journalExercises = myApplication.getJournalExercisesForWidget();
    }

    @Override
    public void onDestroy() {
        //not being used
    }

    @Override
    public int getCount() {
        if (journalExercises == null) {
            return 0;
        }
        return journalExercises.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        JournalDao.JournalExercise journalExercise = journalExercises.get(position);
        RemoteViews remoteViews = new RemoteViews(myApplication.getPackageName(), R.layout.journal_card_row);
        remoteViews.setTextViewText(R.id.exercise_name, journalExercise.name);

        String duration;
        if (journalExercise.totalDuration == 0) {
            duration = myApplication.getString(R.string.not_attempted);
        } else {
            duration = String.format("%02d:%02d",
                    journalExercise.totalDuration / 60,
                    journalExercise.totalDuration % 60);
        }
        remoteViews.setTextViewText(R.id.duration, duration);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        //not being used
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
