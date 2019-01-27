package com.example.android.capstone.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Exercise implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int exerciseId;

    private int program;

    private int orderNumber;

    private String name;

    private long timeLimitInSeconds;

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeLimitInSeconds() {
        return timeLimitInSeconds;
    }

    public void setTimeLimitInSeconds(long timeLimitInSeconds) {
        this.timeLimitInSeconds = timeLimitInSeconds;
    }

    public Exercise(int program, int orderNumber, String name, long timeLimitInSeconds) {
        this.program = program;
        this.orderNumber = orderNumber;
        this.name = name;
        this.timeLimitInSeconds = timeLimitInSeconds;
    }
}
