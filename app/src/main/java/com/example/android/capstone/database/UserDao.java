package com.example.android.capstone.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.capstone.data.UserInfo;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM userinfo ORDER BY userId")
    List<UserInfo> loadAllUserInfo();

    @Insert
    long insertUserInfo(UserInfo userInfo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUserInfo(UserInfo userInfo);

    @Delete
    void deleteUserInfo(UserInfo userInfo);
}
