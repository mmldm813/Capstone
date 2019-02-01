package com.example.android.capstone.journal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.capstone.database.AppDatabase;

import java.sql.Date;
import java.util.List;

public class JournalViewModel extends AndroidViewModel {

    private LiveData<List<Date>> journalEntryDates;

    public JournalViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        journalEntryDates = db.journalDao().loadJournalDates();
    }

    public LiveData<List<Date>> getJournalEntryDates() {
        return journalEntryDates;
    }
}
