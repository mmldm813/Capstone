package com.example.android.capstone.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Date;

@Entity
public class UserInfo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int userId;

    private String name;

    private long feet;

    private long inches;

    private int weightInLbs;

    private Date dateOfBirth;

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

    public enum Gender{
        MALE,
        FEMALE
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
