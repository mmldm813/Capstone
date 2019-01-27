package com.example.android.capstone.dagger;

import android.app.Application;

import com.example.android.capstone.JournalActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {
        Application getApplication();

        void inject(JournalActivity journalActivity);
}
