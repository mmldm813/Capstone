package com.example.android.capstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;
import com.example.android.capstone.database.AppExecutors;

import java.util.List;

public class JournalActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        db = AppDatabase.getInstance(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        performFirstTimeUserExperience();
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
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<UserInfo> userInfo = db.userDao().loadAllUserInfo();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userInfo == null) {
                            UserInfoActivity.startWith(JournalActivity.this);
                        }
                    }
                });
            }
        });
    }
}
