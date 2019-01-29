package com.example.android.capstone.exercise_program;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.capstone.data.Exercise;
import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;

import java.util.List;

import timber.log.Timber;

public class ExerciseProgramViewModel extends AndroidViewModel {

    private LiveData<List<Exercise>> exercises;

    public static class Factory implements ViewModelProvider.Factory {
        private Application application;
        private UserInfo userInfo;


        public Factory(Application application, UserInfo userInfo) {
            this.application = application;
            this.userInfo = userInfo;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ExerciseProgramViewModel(application, userInfo);
        }
    }

    public ExerciseProgramViewModel(@NonNull Application application, UserInfo userInfo) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        Timber.d("Actively retrieving the info from the db.");
        exercises = db.exerciseDao().loadExercisebyProgram(userInfo.getGender().ordinal());
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }

}
