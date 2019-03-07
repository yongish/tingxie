package com.zhiyong.tingxie;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyong.tingxie.db.Pinyin;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.db.Word;
import com.zhiyong.tingxie.ui.main.QuizItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@MediumTest
@RunWith(AndroidJUnit4.class)
public class DBInstrumentedTest {
    private QuizDao mDao;
    private PinyinRoomDatabase mDb;
    private final int testDate = 20190101;
    private long quizId;

    @Before
    public void createDb() {
        mDb = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), PinyinRoomDatabase.class).build();
        mDao = mDb.pinyinDao();

        // Add a quiz.
        quizId = mDao.insert(new Quiz(testDate));
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void blankTest() throws InterruptedException {
        QuizItem result = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(testDate, result.getDate());
        assertEquals(0, result.getTotalWords());
        assertEquals(0, result.getNotLearned());
        assertEquals(1, result.getRound());
    }

    @Test
    public void pinyin4words5() throws InterruptedException {
        // Add 4 pinyins with 5 words.
        String s0 = "jiāo tà shí dì";
        String s1 = "jiǔ niú yì máo";
        String s2 = "yí jiàn zhōng qíng";
        String s3 = "guǐ jì";
        Pinyin p0 = new Pinyin(s0);
        Pinyin p1 = new Pinyin(s1);
        Pinyin p2 = new Pinyin(s2);
        Pinyin p3 = new Pinyin(s3);
        mDao.insert(p0);
        mDao.insert(p1);
        mDao.insert(p2);
        mDao.insert(p3);
        mDao.insert(new Word("脚踏实地", s0));
        mDao.insert(new Word("九牛一毛", s1));
        mDao.insert(new Word("一见钟情", s2));
        mDao.insert(new Word("轨迹", s3));
        mDao.insert(new Word("诡计", s3));

        // Connect all pinyin IDs to the test.
        mDao.insert(new QuizPinyin(quizId, s0));
        mDao.insert(new QuizPinyin(quizId, s1));
        mDao.insert(new QuizPinyin(quizId, s2));
        mDao.insert(new QuizPinyin(quizId, s3));

        QuizItem result = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(testDate, result.getDate());
        assertEquals(4, result.getTotalWords());
        assertEquals(4, result.getNotLearned());
        assertEquals(1, result.getRound());

        mDao.deleteQuizPinyin(quizId, s0);
        result = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(3, result.getTotalWords());
    }

    @Test
    public void getRemainingQuestions1PinyinQuiz() throws InterruptedException {
        String s0 = "jiāo tà shí dì";
        Pinyin p0 = new Pinyin(s0);
        mDao.insert(p0);
        mDao.insert(new Word("脚踏实地", s0));
        mDao.insert(new QuizPinyin(quizId, s0));

        // Answer question correctly.
        mDao.insert(new Question(System.currentTimeMillis(), s0, true, quizId));
        int numberLeft = LiveDataTestUtil.getValue(mDao.getRemainingQuestions(quizId)).size();
        assertEquals(1, numberLeft);
    }

    @Test
    public void getRemainingQuestions2PinyinQuiz() throws InterruptedException {
        String s0 = "jiāo tà shí dì";
        Pinyin p0 = new Pinyin(s0);
        String s1 = "jǐu níu yī máo";
        Pinyin p1 = new Pinyin(s1);
        mDao.insert(p0);
        mDao.insert(p1);
        mDao.insert(new Word("脚踏实地", s0));
        mDao.insert(new Word("九牛一毛", s1));
        mDao.insert(new QuizPinyin(quizId, s0));
        mDao.insert(new QuizPinyin(quizId, s1));

        // Answer question correctly.
        int numberLeftBefore = LiveDataTestUtil.getValue(mDao.getRemainingQuestions(quizId)).size();
        assertEquals(2, numberLeftBefore);
        mDao.insert(new Question(System.currentTimeMillis(), s0, true, quizId));

        int numberLeft = LiveDataTestUtil.getValue(mDao.getRemainingQuestions(quizId)).size();
        assertEquals(1, numberLeft);
    }

    @Test
    public void getRemainingQuestionsAfterAddingWord() throws InterruptedException {
        String s0 = "jiāo tà shí dì";
        Pinyin p0 = new Pinyin(s0);
        mDao.insert(p0);
        mDao.insert(new Word("脚踏实地", s0));
        mDao.insert(new QuizPinyin(quizId, s0));

        int numberLeftBefore = LiveDataTestUtil.getValue(mDao.getRemainingQuestions(quizId)).size();
        assertEquals(1, numberLeftBefore);

        mDao.insert(new Question(System.currentTimeMillis(), s0, true, quizId));

        String s1 = "jǐu níu yī máo";
        Pinyin p1 = new Pinyin(s1);
        mDao.insert(p1);
        mDao.insert(new Word("九牛一毛", s1));
        mDao.insert(new QuizPinyin(quizId, s1));
        int numberLeft = LiveDataTestUtil.getValue(mDao.getRemainingQuestions(quizId)).size();
        assertEquals(1, numberLeft);
    }

    @Test
    public void getAllQuizItemsResetRoundsCompleted() throws InterruptedException {
        String s0 = "jiāo tà shí dì";
        Pinyin p0 = new Pinyin(s0);
        mDao.insert(p0);
        mDao.insert(new Word("脚踏实地", s0));
        mDao.insert(new QuizPinyin(quizId, s0));

        QuizItem quizItem = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(1, quizItem.getRound());

        mDao.insert(new Question(System.currentTimeMillis(), s0, true, quizId));

        String s1 = "jǐu níu yī máo";
        Pinyin p1 = new Pinyin(s1);
        mDao.insert(p1);
        mDao.insert(new Word("九牛一毛", s1));
        mDao.insert(new QuizPinyin(quizId, s1));
        QuizItem quizItem1 = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(1, quizItem1.getRound());
    }

    @Test
    public void updateQuizDate() throws InterruptedException {
        QuizItem quizItem = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(testDate, quizItem.getDate());
        int updatedDate = 20190501;
        Quiz updatedQuiz = new Quiz(updatedDate);
        updatedQuiz.setId(quizId);
        mDao.update(updatedQuiz);
        quizItem = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(updatedDate, quizItem.getDate());
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.zhiyong.tingxie", appContext.getPackageName());
    }

}
