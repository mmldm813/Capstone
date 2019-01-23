package com.example.android.capstone.database;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import com.example.android.capstone.data.UserInfo;

public class GenderConverter {

    @TypeConverter
    public static String fromGenderToString(UserInfo.Gender gender) {
        if (gender == null)
            return null;
        return gender.toString();
    }

    @TypeConverter
    public static UserInfo.Gender fromStringToGender(String gender) {
        if (TextUtils.isEmpty(gender)) {
            return UserInfo.Gender.MALE;
        }

        UserInfo.Gender genderEnum;

        if (gender.equalsIgnoreCase(UserInfo.Gender.MALE.toString())) {
            genderEnum = UserInfo.Gender.MALE;
        } else {
            genderEnum = UserInfo.Gender.FEMALE;
        }

        return genderEnum;
    }

}
