package com.example.android.capstone.exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.android.capstone.R;
import com.example.android.capstone.data.Exercise;

import java.io.Serializable;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    private static final String EXTRA_EXERCISES = "exercises";

    private List<Exercise> exercises;
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
        startNextFragment();
    }

    public void startNextFragment() {
        if (index == exercises.size() - 1) {
            saveToJournal();
            return;
        }

        boolean firstFragment = index == 0;
        Fragment fragment = ExerciseFragment.newInstance(exercises.get(index++));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (firstFragment) {
            fragmentTransaction.add(R.id.fragment_container, fragment, "TAG");
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment, "TAG");
        }
        fragmentTransaction.commit();
    }

    private void saveToJournal() {
        // FIXME
        finish();
    }
}
