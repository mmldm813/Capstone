package com.example.android.capstone.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.example.android.capstone.database.GenderConverter;

import java.io.Serializable;
import java.sql.Date;

@Entity
@TypeConverters(GenderConverter.class)
public class UserInfo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int userId;

    private String name;

    private long feet;

    private long inches;

    private int weightInLbs;

    private Date dateOfBirth;

    private Gender gender;

    public enum Gender {
        MALE("Male"),
        FEMALE("Female");

        private final String databaseKey;

        Gender(final String databaseKey) {
            this.databaseKey = databaseKey;
        }

        public String getDatabaseKey() {
            return databaseKey;
        }
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public long getFeet() {
        return feet;
    }

    public void setFeet(long feet) {
        this.feet = feet;
    }

    public long getInches() {
        return inches;
    }

    public void setInches(long inches) {
        this.inches = inches;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeightInLbs() {
        return weightInLbs;
    }

    public void setWeightInLbs(int weightInLbs) {
        this.weightInLbs = weightInLbs;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
