package com.example.android.capstone.exercise_program;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.capstone.data.Exercise;
import com.example.android.capstone.database.AppDatabase;

import java.util.List;

import timber.log.Timber;

public class ProgramViewModel extends AndroidViewModel {

    private LiveData<List<Exercise>> exercises;

    public ProgramViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        Timber.d("Actively retrieving the info from the db.");
        exercises = db.exerciseDao().loadAllExercises();
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }
}
