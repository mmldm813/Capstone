package com.example.android.capstone.userinfo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;

import java.util.List;

import timber.log.Timber;

public class  UserInfoViewModel extends AndroidViewModel{


    private LiveData<List<UserInfo>> userInfo;

    public UserInfoViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        Timber.d("Actively retrieving the info from the db.");
        userInfo = db.userDao().loadAllUserInfo();
    }

    public LiveData<List<UserInfo>> getUserInfo() {
        return userInfo;
    }
}
