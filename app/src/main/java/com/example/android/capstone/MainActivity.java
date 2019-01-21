package com.example.android.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.capstone.data.UserInfo;
import com.example.android.capstone.database.AppDatabase;

public class MainActivity extends AppCompatActivity {

    private static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(getApplicationContext());

        UserInfo userInfo = new UserInfo();
        userInfo.setName("Michelle");

        long id = db.userDao().insertUserInfo(userInfo);
        Log.d("xxx,","id="+id);
    }
}
