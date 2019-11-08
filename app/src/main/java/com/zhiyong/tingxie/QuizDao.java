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

//    @Update
//    void markQuestionCorrect(Question question);

    @Query("DELETE FROM quiz WHERE id = :quizId")
    void deleteQuiz(long quizId);

    @Query("DELETE FROM quiz_pinyin WHERE quiz_id = :quizId AND pinyin_string = :pinyin")
    void deleteQuizPinyin(long quizId, String pinyin);

    @Query("SELECT * FROM question WHERE uid = :uid")
    LiveData<List<Question>> getAllQuestions(String uid);

    @Query("SELECT Count(correct) FROM question WHERE quiz_id = :quizId AND pinyin_string = :pinyin")
    LiveData<Integer> getCorrectCount(long quizId, String pinyin);

    @Query("SELECT quizId, word, pinyin FROM term WHERE uid = :uid AND quizId = :quizId")
    LiveData<List<WordItem>> getWordItemsOfQuiz(String uid, long quizId);

    @Query("SELECT id, date, title, totalTerms, notLearned, roundsCompleted " +
            "FROM quiz WHERE uid = :uid")
    LiveData<List<QuizItem>> getAllQuizItems(String uid);


    @Query("SELECT quizId, word, pinyin " +
            "FROM term WHERE uid = :uid AND quizId = :quizId AND " +
            "timesCorrect <= (SELECT Max(timesCorrect) FROM term)")
    LiveData<List<WordItem>> getRemainingQuestions(String uid, long quizId);
}
