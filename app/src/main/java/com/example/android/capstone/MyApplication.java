package com.example.android.capstone;

import android.app.Application;

import com.example.android.capstone.database.JournalDao;
import com.example.android.capstone.tts.TtsManager;
import com.example.android.capstone.dagger.AppComponent;
import com.example.android.capstone.dagger.AppModule;
import com.example.android.capstone.dagger.DaggerAppComponent;

import java.sql.Date;
import java.util.List;

import timber.log.Timber;

public class MyApplication extends Application {
    private AppComponent appComponent;
    private AppModule appModule;

    private List<Date> journalEntryDatesForWidget;
    private int widgetCurrentDateIndex;
    private List<JournalDao.JournalExercise> journalExercisesForWidget;

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

    public List<Date> getJournalEntryDatesForWidget() {
        return journalEntryDatesForWidget;
    }

    public void setJournalEntryDatesForWidget(List<Date> journalEntryDatesForWidget) {
        this.journalEntryDatesForWidget = journalEntryDatesForWidget;
    }

    public int getWidgetCurrentDateIndex() {
        return widgetCurrentDateIndex;
    }

    public void setWidgetCurrentDateIndex(int widgetCurrentDateIndex) {
        this.widgetCurrentDateIndex = widgetCurrentDateIndex;
    }

    public List<JournalDao.JournalExercise> getJournalExercisesForWidget() {
        return journalExercisesForWidget;
    }

    public void setJournalExercisesForWidget(List<JournalDao.JournalExercise> journalExercisesForWidget) {
        this.journalExercisesForWidget = journalExercisesForWidget;
    }
}
