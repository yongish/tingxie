package com.zhiyong.tingxie;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.zhiyong.tingxie.db.Pinyin;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.db.Word;

import java.util.List;

@Dao
public interface QuizDao {
    @Insert
    long insert(Quiz quiz);

    @Insert
    long insert(Pinyin pinyin);

    @Insert
    long insert(QuizPinyin quizPinyin);

    @Insert
    void insert(Word word);

    @Insert
    void insert(Question question);

    @Query("SELECT * FROM quiz")
    List<Quiz> getQuizzes();

    @Update
    void markQuestionCorrect(Question question);

    @Delete
    void deleteWord(Word word);

    @Delete
    void deleteQuiz(Quiz quiz);

    @Query("DELETE FROM quiz")
    void deleteAllQuizzes();

    @Query("DELETE FROM pinyin")
    void deleteAllPinyins();

    @Query("DELETE FROM quiz_pinyin")
    void deleteAllQuizPinyins();

    @Query("DELETE FROM word")
    void deleteAllWords();

    @Query("DELETE FROM question")
    void deleteAllQuestions();

    @Query("WITH tpc AS\n" +
            "  (SELECT quiz.id AS quiz_id,\n" +
            "          tp.pinyin_id,\n" +
            "          Count(correct) AS correct_count\n" +
            "   FROM quiz LEFT JOIN quiz_pinyin tp ON quiz.id = tp.quiz_id\n" +
            "   LEFT JOIN question q ON tp.quiz_id = q.quiz_id\n" +
            "   GROUP BY quiz.id,\n" +
            "            tp.pinyin_id),\n" +
            "     tp2 AS\n" +
            "  (SELECT tpc.quiz_id,\n" +
            "          Count(pinyin_id) AS total,\n" +
            "          Min(correct_count) AS rounds_completed\n" +
            "   FROM tpc\n" +
            "   GROUP BY tpc.quiz_id)\n" +
            "SELECT t.date,\n" +
            "       tp2.total AS totalWords,\n" +
            "       Sum(tp2.rounds_completed = tpc.correct_count) AS notLearned,\n" +
            "       tp2.rounds_completed + 1 AS round\n" +
            "FROM tpc\n" +
            "LEFT JOIN tp2 ON tp2.quiz_id = tpc.quiz_id\n" +
            "JOIN quiz t ON t.id = tp2.quiz_id\n" +
            "GROUP BY tp2.quiz_id;\n")
    LiveData<List<QuizItem>> getAllQuizItems();
}
