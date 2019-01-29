package com.example.android.capstone.journal;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.capstone.data.Journal;

import java.sql.Date;
import java.util.List;

@Dao
public interface JournalDao {

    @Query("SELECT * FROM journal ORDER BY id")
    LiveData<List<Journal>> loadAllJournalEntry();

    @Insert
    long insertJournalEntry(Journal journal);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournalEntry(Journal journal);

    @Delete
    void deleteJournalEntry(Journal journal);

    @Query("SELECT * from journal INNER JOIN Exercise ON journal.exerciseId = exercise.exerciseId WHERE journal.attempt = :attempt")
    LiveData<List<JournalExercise>> loadJournalEntryAndExerciseByAttempt(int attempt);

    class JournalExercise {
        public Date date;
        public int attempt;
        public int exerciseId;
        public long totalDuration;
        public String name;
        public long timeLimitInSeconds;
    }
}
