package com.zhiyong.tingxie;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.db.QuizRole;
import com.zhiyong.tingxie.db.Word;
import com.zhiyong.tingxie.ui.word.WordItem;

import java.util.List;

// todo: 12/2/22. Delete this file after finish porting from local to remote.
@Dao
public interface QuizDao {
    @Insert
    long insert(Quiz quiz);

    @Insert
    long insert(QuizRole quizRole);

    @Update
    void update(Quiz quiz);

    @Insert
    long insert(QuizPinyin quizPinyin);

    @Query("UPDATE quiz_pinyin SET asked = 0 WHERE quiz_id = :quizId")
    int resetAsked(long quizId);

    @Query("UPDATE quiz_pinyin SET asked = :asked WHERE quiz_id = :quizId AND pinyin_string = :pinyinString")
    void updateQuizPinyin(long quizId, String pinyinString, boolean asked);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Word word);

    @Insert
    void insert(Question question);

    @Query("UPDATE question SET reset_time = :timestamp WHERE quiz_id = :quizId")
    void updateQuestions(long quizId, long timestamp);

    @Query("DELETE FROM quiz WHERE id = :quizId")
    void deleteQuiz(long quizId);

    @Query("DELETE FROM quiz_pinyin WHERE quiz_id = :quizId AND pinyin_string = :pinyin")
    void deleteQuizPinyin(long quizId, String pinyin);

    @Query("DELETE FROM quiz_pinyin WHERE quiz_id = :quizId")
    void deleteQuizPinyins(long quizId);

    @Query("SELECT * FROM quiz_pinyin")
    LiveData<List<QuizPinyin>> getAllQuizPinyins();

    @Query("SELECT * FROM question")
    LiveData<List<Question>> getAllQuestions();

    @Query("SELECT Count(correct) FROM question WHERE quiz_id = :quizId AND pinyin_string = :pinyin")
    LiveData<Integer> getCorrectCount(long quizId, String pinyin);

    @Query("SELECT DISTINCT q.id AS quizId,\n" +
            "                qp.id AS id,\n" +
            "                qp.word_string AS wordString,\n" +
            "                qp.pinyin_string AS pinyinString,\n" +
            "                qp.asked\n" +
            "FROM quiz q\n" +
            "JOIN quiz_pinyin qp ON q.id = qp.quiz_id\n" +
//            "JOIN word w ON qp.pinyin_string = w.pinyin_string\n" +
            "WHERE q.id = :quizId\n" +
            "ORDER BY qp.pinyin_string")
//            "ORDER BY w.pinyin_string")
    LiveData<List<WordItem>> getWordItemsOfQuiz(long quizId);

    @Query("SELECT id, date, title, total_words, not_learned, round " +
            "FROM quiz ORDER BY date DESC")
    LiveData<List<Quiz>> getAllQuizItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Quiz> quizzes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuizPinyins(List<QuizPinyin> quizPinyins);

//    @Query("SELECT id, date, title, total_words AS numWords, not_learned AS numNotCorrect, round " +
//            "FROM quiz WHERE id = :id")
//    LiveData<QuizItem> getQuizItem(long id);

    @Query("SELECT :quizId AS quizId," +
            "qp.id AS id," +
            "word_string AS wordString, pinyin_string AS pinyinString, asked " +
            "FROM quiz_pinyin qp WHERE qp.asked = 0 AND qp.quiz_id = :quizId")
    LiveData<List<WordItem>> getRemainingQuestions(long quizId);
}
