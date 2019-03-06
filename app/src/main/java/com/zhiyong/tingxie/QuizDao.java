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
import com.zhiyong.tingxie.ui.main.QuizItem;
import com.zhiyong.tingxie.ui.word.WordItem;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface QuizDao {
    @Insert
    long insert(Quiz quiz);

    @Update
    void update(Quiz quiz);

    @Insert(onConflict = IGNORE)
    long insert(Pinyin pinyin);

    @Insert
    long insert(QuizPinyin quizPinyin);

    @Insert
    void insert(Word word);

    @Insert
    void insert(Question question);

    @Update
    void markQuestionCorrect(Question question);

    @Delete
    void deleteWord(Word word);

    @Query("DELETE FROM quiz WHERE id = :quizId")
    void deleteQuiz(long quizId);

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

    @Query("DELETE FROM quiz_pinyin WHERE quiz_id = :quizId AND pinyin_string = :pinyin")
    void deleteQuizPinyin(long quizId, String pinyin);

    @Query("SELECT * FROM quiz_pinyin")
    LiveData<List<QuizPinyin>> getAllQuizPinyins();

    @Query("SELECT * FROM question")
    LiveData<List<Question>> getAllQuestions();

    @Query("SELECT DISTINCT q.id AS quizId,\n" +
            "                w.word_string AS wordString,\n" +
            "                p.pinyin_string AS pinyinString\n" +
            "FROM quiz q\n" +
            "JOIN quiz_pinyin qp ON q.id = qp.quiz_id\n" +
            "JOIN pinyin p ON qp.pinyin_string = p.pinyin_string\n" +
            "JOIN word w ON p.pinyin_string = w.pinyin_string\n" +
            "WHERE q.id = :quizId\n" +
            "ORDER BY p.pinyin_string")
    LiveData<List<WordItem>> getWordItemsOfQuiz(int quizId);

    @Query("WITH tpc AS\n" +
            "  (SELECT quiz.id AS quiz_id,\n" +
            "          tp.pinyin_string,\n" +
            "          Count(correct) AS correct_count\n" +
            "   FROM quiz\n" +
            "   LEFT JOIN quiz_pinyin tp ON quiz.id = tp.quiz_id\n" +
            "   LEFT JOIN question q ON tp.quiz_id = q.quiz_id\n" +
            "   AND tp.pinyin_string = q.pinyin_string\n" +
            "   GROUP BY quiz.id,\n" +
            "            tp.pinyin_string),\n" +
            "     tp2 AS\n" +
            "  (SELECT tpc.quiz_id,\n" +
            "          Count(tpc.pinyin_string) AS total,\n" +
            "          Min(correct_count) AS rounds_completed\n" +
            "   FROM tpc\n" +
            "   GROUP BY tpc.quiz_id)\n" +
            "SELECT t.id,\n" +
            "       t.date,\n" +
            "       tp2.total AS totalWords,\n" +
            "       Min(tp2.total, Count(tp2.rounds_completed = tpc.correct_count)) AS notLearned,\n" +
            "       tp2.rounds_completed + 1 AS round\n" +
            "FROM tpc\n" +
            "LEFT JOIN tp2 ON tp2.quiz_id = tpc.quiz_id\n" +
            "JOIN quiz t ON t.id = tp2.quiz_id\n" +
            "GROUP BY tp2.quiz_id\n" +
            "ORDER BY t.date")
    LiveData<List<QuizItem>> getAllQuizItems();

    @Query("WITH tpc AS\n" +
            "  (SELECT tp.pinyin_string,\n" +
            "          Count(correct) AS correct_count\n" +
            "   FROM quiz_pinyin tp LEFT JOIN question q ON tp.quiz_id = q.quiz_id\n" +
            "   WHERE tp.quiz_id = :quizId\n" +
            "   GROUP BY tp.pinyin_string),\n" +
            "     tp2 AS\n" +
            "  (SELECT Min(correct_count) AS rounds_completed\n" +
            "   FROM tpc),\n" +
            "     tp3 AS\n" +
            "  (SELECT tpc.pinyin_string\n" +
            "   FROM tpc\n" +
            "   WHERE tpc.correct_count =\n" +
            "       (SELECT rounds_completed\n" +
            "        FROM tp2)\n" +
            "   )\n" +
            "SELECT :quizId AS quizId,\n" +
            "       w.word_string AS wordString," +
            "       tp3.pinyin_string AS pinyinString\n" +
            "FROM tp3\n" +
            "JOIN word w ON tp3.pinyin_string = w.pinyin_string")
    LiveData<List<WordItem>> getPossibleQuestions(int quizId);

    @Query("WITH tpc AS\n" +
            "  (SELECT qp.pinyin_string,\n" +
            "          qn.pinyin_string,\n" +
            "          Count(correct) AS correct_count\n" +
            "   FROM quiz_pinyin qp\n" +
            "   LEFT JOIN question qn ON qp.quiz_id = qn.quiz_id\n" +
            "   AND qp.pinyin_string = qn.pinyin_string\n" +
            "   WHERE qp.quiz_id = :quizId\n" +
            "   GROUP BY qp.pinyin_string),\n" +
            "     tp2 AS\n" +
            "  (SELECT Min(correct_count) AS rounds_completed\n" +
            "   FROM tpc),\n" +
            "     tp3 AS\n" +
            "  (SELECT tpc.pinyin_string\n" +
            "   FROM tpc\n" +
            "   WHERE tpc.correct_count =\n" +
            "       (SELECT rounds_completed\n" +
            "        FROM tp2) )\n" +
            "SELECT :quizId AS quizId,\n" +
            "       w.word_string AS wordString,\n" +
            "       tp3.pinyin_string AS pinyinString\n" +
            "FROM tp3\n" +
            "JOIN word w ON tp3.pinyin_string = w.pinyin_string")
    LiveData<List<WordItem>> getRemainingQuestions(long quizId);
}
