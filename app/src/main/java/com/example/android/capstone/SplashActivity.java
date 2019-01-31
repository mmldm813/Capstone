package com.example.android.capstone;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Window;

import com.example.android.capstone.database.AppDatabase;
import com.example.android.capstone.database.ExerciseHelper;
import com.example.android.capstone.tts.TtsManager;


public class SplashActivity extends AppCompatActivity {
    private TtsManager ttsManager;
    private Handler handler;
    private ExerciseHelper exerciseHelper;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

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
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(getPackageName(),
                "com.example.android.capstone.journal.JournalActivity"));
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ttsManager.checkTextToSpeechInstalledResult(requestCode, resultCode);
    }
}
