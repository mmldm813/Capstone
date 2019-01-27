package com.example.android.capstone.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.capstone.data.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Query("SELECT * FROM exercise ORDER BY exerciseId")
    List<Exercise> loadAllExercises();

    @Insert
    long insertExercises(Exercise exercise);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateExercises(Exercise exercise);

    @Delete
    void deleteExercises(Exercise exercise);
}
