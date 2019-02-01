package com.example.android.capstone.journal;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.capstone.R;
import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;
import com.example.android.capstone.database.AppExecutors;
import com.example.android.capstone.exercise_program.ExerciseProgramActivity;
import com.example.android.capstone.userinfo.UserInfoActivity;

import java.util.List;

public class JournalActivityBase extends AppCompatActivity {

    private JournalViewModel viewModel;
    private UserInfo userInfo;
    private FloatingActionButton fab;
    private CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_layout);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        viewModel = ViewModelProviders.of(this).get(JournalViewModel.class);

        fab = findViewById(R.id.fab_button);
        fabButtonNavigation();

        observeJournalEntries();

        cardAdapter = new CardAdapter(this);
        setupJournalCards();
    }

    private void observeJournalEntries() {
        viewModel.getJournalEntryDates().observe(this, new Observer<List<java.sql.Date>>() {
            @Override
            public void onChanged(@Nullable final List<java.sql.Date> dates) {
                cardAdapter.setData(dates);
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

    @Override
    protected void onResume() {
        super.onResume();
        performFirstTimeUserExperience();
    }

    private void performFirstTimeUserExperience() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(JournalActivityBase.this);
                List<UserInfo> userInfos = db.userDao().loadAllUserInfoFirstTimeUserExperience();
                if (userInfos.size() == 0) {
                    UserInfoActivity.startWith(JournalActivityBase.this);
                    return;
                }
                userInfo = userInfos.get(0);
            }
        });
    }

    private void setupJournalCards() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager list = new LinearLayoutManager(this);
        list.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(list);
        recyclerView.setAdapter(cardAdapter);
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
