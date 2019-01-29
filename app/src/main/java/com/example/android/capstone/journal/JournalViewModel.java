package com.example.android.capstone.journal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;

import java.util.List;

public class JournalViewModel extends AndroidViewModel {

    private LiveData<List<UserInfo>> userInfos;
    private LiveData<List<JournalDao.JournalExercise>> journalEntries;

    public JournalViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        userInfos = db.userDao().loadAllUserInfo();
        journalEntries = db.journalDao().loadJournalEntryAndExerciseByAttempt(1);
    }

    public LiveData<List<JournalDao.JournalExercise>> getJournalEntries() {
        return journalEntries;
    }

    public LiveData<List<UserInfo>> getUserInfos() {
        return userInfos;
    }
}
