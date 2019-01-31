package com.example.android.capstone.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.capstone.R;
import com.example.android.capstone.data.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseHelper {

    private AppCompatActivity activity;
    private AppDatabase db;
    private Runnable onLoadComplete;

    public ExerciseHelper(AppCompatActivity activity, AppDatabase db, Runnable onLoadComplete) {
        this.activity = activity;
        this.db = db;
        this.onLoadComplete = onLoadComplete;
    }

    public void loadDataIfEmpty() {
        final LiveData<List<Exercise>> exercises = db.exerciseDao().loadAllExercises();
        exercises.observe(activity, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                if (exercises.size() == 0) {
                    loadData();
                } else {
                    onLoadComplete.run();
                }
            }
        });

    }

    private void loadData() {
        final List<Exercise> exercises = new ArrayList<>();
        //Male Program
        exercises.add(new Exercise(0, 0, activity.getString(R.string.one_half_mile_run),788));
        exercises.add(new Exercise(0, 1,activity.getString(R.string.five_min_rest),300));
        exercises.add(new Exercise(0, 2, activity.getString(R.string.twentysix_pushups), 60));
        exercises.add(new Exercise(0, 3,activity.getString(R.string.five_min_rest),300));
        exercises.add(new Exercise(0, 4,activity.getString(R.string.thirtyfive_situps),60));
        exercises.add(new Exercise(0, 5,activity.getString(R.string.five_min_rest),300));
        exercises.add(new Exercise(0, 6,activity.getString(R.string.three_hund_meter_run),62));

        //Female Program
        exercises.add(new Exercise(1, 0, activity.getString(R.string.one_half_mile_run),956));
        exercises.add(new Exercise(1, 1,activity.getString(R.string.five_min_rest),300));
        exercises.add(new Exercise(1, 2, activity.getString(R.string.thirteen_pushups), 60));
        exercises.add(new Exercise(1, 3,activity.getString(R.string.five_min_rest),300));
        exercises.add(new Exercise(1, 4,activity.getString(R.string.thirty_situps),60));
        exercises.add(new Exercise(1, 5,activity.getString(R.string.five_min_rest),300));
        exercises.add(new Exercise(1, 6,activity.getString(R.string.three_hund_meter_run),75));

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for(Exercise exercise : exercises) {
                    db.exerciseDao().insertExercises(exercise);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });
    }

    private void finish() {
        onLoadComplete.run();
    }

}
