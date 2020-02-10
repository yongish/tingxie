package com.zhiyong.tingxie;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zhiyong.tingxie.db.Pinyin;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.db.Word;
import com.zhiyong.tingxie.ui.main.QuizItem;
import com.zhiyong.tingxie.ui.word.WordItem;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Word word);

    @Insert
    void insert(Question question);

    @Query("UPDATE question SET reset_time = :timestamp WHERE quiz_id = :quizId")
    void updateQuestions(long quizId, long timestamp);

    @Update
    void markQuestionCorrect(Question question);

    @Delete
    void deleteWord(Word word);

    @Query("DELETE FROM quiz WHERE id = :quizId")
    void deleteQuiz(long quizId);

    @Query("DELETE FROM quiz")
    void deleteAllQuizzes();

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

    @Query("SELECT Count(correct) FROM question WHERE quiz_id = :quizId AND pinyin_string = :pinyin")
    LiveData<Integer> getCorrectCount(long quizId, String pinyin);

    @Query("SELECT DISTINCT q.id AS quizId,\n" +
            "                w.word_string AS wordString,\n" +
            "                w.pinyin_string AS pinyinString\n" +
            "FROM quiz q\n" +
            "JOIN quiz_pinyin qp ON q.id = qp.quiz_id\n" +
            "JOIN word w ON qp.pinyin_string = w.pinyin_string\n" +
            "WHERE q.id = :quizId\n" +
            "ORDER BY w.pinyin_string")
    LiveData<List<WordItem>> getWordItemsOfQuiz(long quizId);

    @Query("SELECT id, date, title, total_words, not_learned, round FROM quiz")
    LiveData<List<QuizItem>> getAllQuizItems();

    @Query("WITH tpc AS\n" +
            "  (SELECT tp.pinyin_string,\n" +
            "          Count(correct) AS correct_count\n" +
            "   FROM quiz_pinyin tp\n" +
            "   LEFT JOIN question q ON tp.quiz_id = q.quiz_id\n" +
            "   AND q.reset_time <= q.timestamp\n" +
            "   WHERE tp.quiz_id = :quizId\n" +
            "   GROUP BY tp.pinyin_string)\n" +
            "SELECT :quizId AS quizId,\n" +
            "       tpc.pinyin_string,\n" +
            "       w.word_string AS wordString\n" +
            "FROM tpc\n" +
            "JOIN word w ON tpc.pinyin_string = w.pinyin_string\n" +
            "WHERE tpc.correct_count =\n" +
            "    (SELECT Min(correct_count)\n" +
            "     FROM tpc)")
    LiveData<List<WordItem>> getPossibleQuestions(long quizId);

    // todo: REWRITE THIS NEXT.
    @Query("WITH tpc AS\n" +
            "  (SELECT qp.pinyin_string,\n" +
            "          qn.pinyin_string,\n" +
            "          Count(correct) AS correct_count\n" +
            "   FROM quiz_pinyin qp\n" +
            "   LEFT JOIN question qn ON qp.quiz_id = qn.quiz_id\n" +
            "   AND qp.pinyin_string = qn.pinyin_string\n" +
            "   AND qn.reset_time <= qn.timestamp\n" +
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
