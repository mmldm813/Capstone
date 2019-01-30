package com.example.android.capstone.journal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;

import java.sql.Date;
import java.util.List;

public class JournalViewModel extends AndroidViewModel {

    private LiveData<List<UserInfo>> userInfos;
    private LiveData<List<Date>> journalEntryDates;

    public JournalViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        userInfos = db.userDao().loadAllUserInfo();
        journalEntryDates = db.journalDao().loadJournalDates();
    }

    public LiveData<List<UserInfo>> getUserInfos() {
        return userInfos;
    }

    public LiveData<List<Date>> getJournalEntryDates() {
        return journalEntryDates;
    }
}
