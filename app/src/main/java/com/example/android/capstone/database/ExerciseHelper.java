package com.example.android.capstone.database;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.capstone.data.Exercise;
import com.example.android.capstone.exercise_program.ProgramViewModel;

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
        ProgramViewModel viewModel = ViewModelProviders.of(activity).get(ProgramViewModel.class);
        viewModel.getExercises().observe(activity, new Observer<List<Exercise>>() {
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
        exercises.add(new Exercise(1, 0, "1.5 Mile Run",788));
        exercises.add(new Exercise(1, 1,"5 Minute Rest",300));
        exercises.add(new Exercise(1, 2, "26 Push Ups", 60));
        exercises.add(new Exercise(1, 3,"5 Minute Rest",300));
        exercises.add(new Exercise(1, 4,"35 Sit Ups",60));
        exercises.add(new Exercise(1, 5,"5 Minute Rest",300));
        exercises.add(new Exercise(1, 6,"300 meter Run",62));

        //Female Program
        exercises.add(new Exercise(2, 0, "1.5 Mile Run",956));
        exercises.add(new Exercise(2, 1,"5 Minute Rest",300));
        exercises.add(new Exercise(2, 2, "13 Push Ups", 60));
        exercises.add(new Exercise(2, 3,"5 Minute Rest",300));
        exercises.add(new Exercise(2, 4,"30 Sit Ups",60));
        exercises.add(new Exercise(2, 5,"5 Minute Rest",300));
        exercises.add(new Exercise(2, 6,"300 meter Run",75));

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for(Exercise exercise : exercises) {
                    db.exerciseDao().insertExercises(exercise);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onLoadComplete.run();
                    }
                });
            }
        });
    }

}
