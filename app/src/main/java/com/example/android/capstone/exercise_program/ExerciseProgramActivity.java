package com.example.android.capstone.exercise_program;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.android.capstone.R;
import com.example.android.capstone.data.Exercise;
import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;
import com.example.android.capstone.exercise.ExerciseActivity;

import java.util.List;

public class ExerciseProgramActivity extends AppCompatActivity {
    private static final String EXTRA_USER_INFO = "userinfo";

    private AppDatabase db;
    private UserInfo userInfo;
    private ExerciseAdapter adapter;
    private List<Exercise> exercises;
    private Button startButton;

    public static void startWith(Activity activity, UserInfo userInfo) {
        Intent intent = new Intent(activity, ExerciseProgramActivity.class);
        intent.putExtra(EXTRA_USER_INFO, userInfo);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_layout);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = AppDatabase.getInstance(getApplicationContext());
        userInfo = (UserInfo) getIntent().getSerializableExtra(EXTRA_USER_INFO);
        adapter = new ExerciseAdapter(this);

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseActivity.startWith(ExerciseProgramActivity.this, exercises);
                finish();
            }
        });

        ExerciseProgramViewModel viewModel = ViewModelProviders.of(this,
                new ExerciseProgramViewModel.Factory(this.getApplication(), userInfo)).get(ExerciseProgramViewModel.class);
        viewModel.getExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                ExerciseProgramActivity.this.exercises = exercises;
                adapter.setData(exercises);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.program_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
