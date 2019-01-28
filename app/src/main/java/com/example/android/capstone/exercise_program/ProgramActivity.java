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

import com.example.android.capstone.R;
import com.example.android.capstone.data.Exercise;
import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;

import java.util.List;

public class ProgramActivity extends AppCompatActivity {
    private static final String EXTRA_USER_INFO = "userinfo";

    private AppDatabase db;
    private UserInfo userInfo;
    private ExerciseAdapter adapter;

    public static void startWith(Activity activity, UserInfo userInfo) {
        Intent intent = new Intent(activity, ProgramActivity.class);
        intent.putExtra(EXTRA_USER_INFO, userInfo);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_layout);

        db = AppDatabase.getInstance(getApplicationContext());
        userInfo = (UserInfo) getIntent().getSerializableExtra(EXTRA_USER_INFO);
        adapter = new ExerciseAdapter(this);

        ProgramViewModel viewModel = ViewModelProviders.of(this,
                new ProgramViewModel.Factory(this.getApplication(), userInfo)).get(ProgramViewModel.class);
        viewModel.getExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
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
