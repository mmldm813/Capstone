package com.example.android.capstone.journal;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.capstone.R;
import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.exercise_program.ExerciseProgramActivity;
import com.example.android.capstone.userinfo.UserInfoActivity;

import java.util.List;

import timber.log.Timber;

public class JournalActivityBase extends AppCompatActivity {

    private JournalViewModel viewModel;
    private UserInfo userInfo;
    private FloatingActionButton fab;

    // FIXME - add spinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_layout);

        viewModel = ViewModelProviders.of(this).get(JournalViewModel.class);

        fab = findViewById(R.id.fab_button);
        fabButtonNavigation();

        performFirstTimeUserExperience();
        observeJournalEntries();
    }

    private void observeJournalEntries() {
        viewModel.getJournalEntries().observe(this, new Observer<List<JournalDao.JournalExercise>>() {
            @Override
            public void onChanged(@Nullable List<JournalDao.JournalExercise> journalEntries) {
                Timber.d("xxx");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_userinfo:
                UserInfoActivity.startWith(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void performFirstTimeUserExperience() {
        viewModel.getUserInfos().observe(this, new Observer<List<UserInfo>>() {
            @Override
            public void onChanged(@Nullable List<UserInfo> userInfos) {
                Timber.d("Receiving database update from LiveData");
                if (userInfos.size() == 0) {
                    UserInfoActivity.startWith(JournalActivityBase.this);
                    return;
                }
                userInfo = userInfos.get(0);
            }
        });
    }

    private void fabButtonNavigation() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseProgramActivity.startWith(JournalActivityBase.this, userInfo);
            }
        });
    }
}
