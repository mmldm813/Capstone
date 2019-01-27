package com.example.android.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.capstone.coach.TtsManager;
import com.example.android.capstone.database.AppDatabase;
import com.example.android.capstone.database.ExerciseHelper;


public class SplashActivity extends AppCompatActivity {
    private TtsManager ttsManager;
    private Handler handler;
    private ExerciseHelper exerciseHelper;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        db = AppDatabase.getInstance(getApplicationContext());

        handler = new Handler();
        ttsManager = new TtsManager();
        ttsManager.init(this, new TtsManager.Callbacks() {
            @Override
            public void onInitCompleted(boolean success) {
                ((MyApplication)getApplicationContext()).setTtsManager(ttsManager);
                exerciseHelper = new ExerciseHelper(SplashActivity.this, db, new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startNextActivity();
                            }
                        }, 2000);
                    }
                });
                exerciseHelper.loadDataIfEmpty();
            }
        });
    }

    private void startNextActivity() {
        startActivity(new Intent(SplashActivity.this, JournalActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ttsManager.checkTextToSpeechInstalledResult(requestCode, resultCode);
    }
}
