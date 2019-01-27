package com.example.android.capstone.dagger;

import android.app.Application;

import com.example.android.capstone.coach.TtsManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application application;
    private TtsManager ttsManager;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public TtsManager provideTtsManager() {
        return ttsManager;
    }

    public void setTtsManager(TtsManager ttsManager) {
        this.ttsManager = ttsManager;
    }
}
