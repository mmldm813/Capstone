package com.example.android.capstone.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.android.capstone.database.DateConverter;

import java.io.Serializable;
import java.sql.Date;

@Entity (foreignKeys = @ForeignKey(entity = Exercise.class, parentColumns = "exerciseId", childColumns = "exerciseId"))
@TypeConverters(DateConverter.class)
public class Journal implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date;
    private int attempt;
    private int exerciseId;
    private long totalDuration;

    public Journal(Date date, int attempt, int exerciseId, long totalDuration){
        this.date = date;
        this.attempt = attempt;
        this.exerciseId = exerciseId;
        this.totalDuration = totalDuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }
}
