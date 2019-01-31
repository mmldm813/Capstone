package com.example.android.capstone.exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.android.capstone.R;
import com.example.android.capstone.data.Exercise;
import com.example.android.capstone.data.Journal;
import com.example.android.capstone.database.AppDatabase;
import com.example.android.capstone.database.AppExecutors;
import com.example.android.capstone.database.JournalDao;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {
    private static final String EXTRA_EXERCISES = "exercises";
    private static final String EXTRA_DURATIONS = "durations";
    private static final String EXTRA_INDEX = "index";

    private List<Exercise> exercises;
    private ArrayList<Long> durations;
    private int index;

    public static void startWith(Activity activity, List<Exercise> exercises) {
        Intent intent = new Intent(activity, ExerciseActivity.class);
        intent.putExtra(EXTRA_EXERCISES, (Serializable) exercises);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_layout);

        exercises = (List<Exercise>) getIntent().getSerializableExtra(EXTRA_EXERCISES);
        if (savedInstanceState == null) {
            durations = new ArrayList(exercises.size());

            startFirstFragment();
        } else {
            durations = (ArrayList<Long>) savedInstanceState.getSerializable(EXTRA_DURATIONS);
            index = savedInstanceState.getInt(EXTRA_INDEX);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_DURATIONS, durations);
        outState.putInt(EXTRA_INDEX, index);
        super.onSaveInstanceState(outState);
    }

    private void startFirstFragment() {
        Fragment fragment = ExerciseFragment.newInstance(exercises.get(index));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment, "TAG");
        fragmentTransaction.commit();
    }

    public void startNextFragment(long timeRemaining) {
        long duration = exercises.get(index).getTimeLimitInSeconds() - timeRemaining;
        durations.add(index, duration);

        index++;
        if (index == exercises.size()) {
            saveToJournal();
            return;
        }

        Fragment fragment = ExerciseFragment.newInstance(exercises.get(index));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, "TAG");
        fragmentTransaction.commit();
    }

    // FIXME - attempt
    private void saveToJournal() {
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        final JournalDao journalDao = db.journalDao();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Date today = new Date(System.currentTimeMillis());
                for(int i = 0; i < exercises.size(); i++) {
                    Exercise exercise = exercises.get(i);
                    Journal journal = new Journal(today, 1, exercise.getExerciseId(), durations.get(i));
                    journalDao.insertJournalEntry(journal);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });
    }
}
