package com.zhiyong.tingxie;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TestDao {
    @Insert
    long insert(Test test);


    @Update
    void markQuestionCorrect(Question question);

    @Delete
    void deleteWord(Word word);

    @Delete
    void deleteTest(Test test);

    @Query("DELETE FROM test")
    void deleteAll();

    @Query("WITH tpc AS\n" +
            "  (SELECT test.id AS test_id,\n" +
            "          tp.pinyin_id,\n" +
            "          Count(correct) AS correct_count\n" +
            "   FROM test LEFT JOIN test_pinyin tp ON test.id = tp.test_id\n" +
            "   LEFT JOIN question q ON tp.test_id = q.test_id\n" +
            "   GROUP BY test.id,\n" +
            "            tp.pinyin_id),\n" +
            "     tp2 AS\n" +
            "  (SELECT tpc.test_id,\n" +
            "          Count(pinyin_id) AS total,\n" +
            "          Min(correct_count) AS rounds_completed\n" +
            "   FROM tpc\n" +
            "   GROUP BY tpc.test_id)\n" +
            "SELECT t.date,\n" +
            "       tp2.total AS totalWords,\n" +
            "       Sum(tp2.rounds_completed > 0 AND tp2.rounds_completed = tpc.correct_count) AS notLearned,\n" +
            "       tp2.rounds_completed + 1 AS round\n" +
            "FROM tpc\n" +
            "LEFT JOIN tp2 ON tp2.test_id = tpc.test_id\n" +
            "JOIN test t ON t.id = tp2.test_id\n" +
            "GROUP BY tp2.test_id;")
    LiveData<List<TestItem>> getAllTestItems();
}