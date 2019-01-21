package com.example.android.capstone.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity
public class Program implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String programName;

    private List<Exercise> exercises;
}
