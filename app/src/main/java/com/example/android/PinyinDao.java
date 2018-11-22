package com.example.android;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PinyinDao {
    @Insert
    void insert(Test test);

    @Update
    void markQuestionCorrect(Question question);

    @Delete
    void deleteWord(Word word);

    @Delete
    void deleteTest(Test test);

    @Query("DELETE FROM test")
    void deleteAll();

    @Query("SELECT * FROM test")
    LiveData<List<Test>> getAllTests();
}
