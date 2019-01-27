package com.example.android.capstone;

import android.app.Application;

import com.example.android.capstone.coach.TtsManager;
import com.example.android.capstone.dagger.AppComponent;
import com.example.android.capstone.dagger.AppModule;
import com.example.android.capstone.dagger.DaggerAppComponent;

import timber.log.Timber;

public class MyApplication extends Application {
    private AppComponent appComponent;
    private AppModule appModule;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        appModule = new AppModule(this);
        appComponent = DaggerAppComponent.builder()
                .appModule(appModule)
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public void setTtsManager(TtsManager ttsManager) {
        appModule.setTtsManager(ttsManager);
    }
}
