package com.zhiyong.tingxie;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.MediumTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.db.Word;
import com.zhiyong.tingxie.ui.main.QuizItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
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
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private QuizDao mDao;
    private PinyinRoomDatabase mDb;
    private final int testDate = 20190101;
    private long quizId;

    @Before
    public void createDb() {
        mDb = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                PinyinRoomDatabase.class).allowMainThreadQueries().build();
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
    public void getRemainingQuestions1PinyinQuiz() throws InterruptedException {
        String s0 = "jiāo tà shí dì";
        String w0 = "脚踏实地";
        mDao.insert(new Word(w0, s0));
        mDao.insert(new QuizPinyin(quizId, s0, w0, false));
        long timestamp = System.currentTimeMillis();

        // Answer question correctly.
        mDao.insert(new Question(timestamp, timestamp, s0, true, quizId));
        int numberLeft = LiveDataTestUtil.getValue(mDao.getRemainingQuestions(quizId)).size();

        // todo: SHOULD BE 0!!!
        assertEquals(1, numberLeft);
    }

    @Test
    public void getRemainingQuestions2PinyinQuiz() throws InterruptedException {
        String s0 = "jiāo tà shí dì";
        String s1 = "jǐu níu yī máo";
        String w0 = "脚踏实地";
        String w1 = "九牛一毛";
        mDao.insert(new Word("脚踏实地", s0));
        mDao.insert(new Word("九牛一毛", s1));
        mDao.insert(new QuizPinyin(quizId, s0, w0, false));
        mDao.insert(new QuizPinyin(quizId, s1, w1, false));

        // Answer question correctly.
        int numberLeftBefore = LiveDataTestUtil.getValue(mDao.getRemainingQuestions(quizId)).size();
        assertEquals(2, numberLeftBefore);
        long ts = System.currentTimeMillis();
        mDao.insert(new Question(ts, ts, s0, true, quizId));

        int numberLeft = LiveDataTestUtil.getValue(mDao.getRemainingQuestions(quizId)).size();
        assertEquals(1, numberLeft);
    }

    @Test
    public void getRemainingQuestionsAfterAddingWord() throws InterruptedException {
        String s0 = "jiāo tà shí dì";
        String w0 = "脚踏实地";
        mDao.insert(new Word(w0, s0));
        mDao.insert(new QuizPinyin(quizId, s0, w0, false));

        int numberLeftBefore = LiveDataTestUtil.getValue(mDao.getRemainingQuestions(quizId)).size();
        assertEquals(1, numberLeftBefore);

        long ts = System.currentTimeMillis();
        mDao.insert(new Question(ts, ts, s0, true, quizId));

        String s1 = "jǐu níu yī máo";
        String w1 = "九牛一毛";
        mDao.insert(new Word(w1, s1));
        mDao.insert(new QuizPinyin(quizId, s1, w1, false));
        int numberLeft = LiveDataTestUtil.getValue(mDao.getRemainingQuestions(quizId)).size();
        assertEquals(1, numberLeft);
    }

    @Test
    public void getAllQuizItemsResetRoundsCompleted() throws InterruptedException {
        String s0 = "jiāo tà shí dì";
        String w0 = "脚踏实地";
        mDao.insert(new Word(w0, s0));
        mDao.insert(new QuizPinyin(quizId, s0, w0, false));

        QuizItem quizItem = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(1, quizItem.getRound());

        long ts = System.currentTimeMillis();
        mDao.insert(new Question(ts, ts, s0, true, quizId));

        String s1 = "jǐu níu yī máo";
        String w1 = "九牛一毛";
        mDao.insert(new Word(w1, s1));
        mDao.insert(new QuizPinyin(quizId, s1, w1, false));
        QuizItem quizItem1 = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(1, quizItem1.getRound());
    }

    @Test
    public void addWordResetsCorrectCounters() throws InterruptedException {
        String s0 = "jiāo tà shí dì";
        String w0 = "脚踏实地";
        mDao.insert(new Word(w0, s0));
        mDao.insert(new QuizPinyin(quizId, s0, w0, false));

        long ts = System.currentTimeMillis();
        mDao.insert(new Question(ts, ts, s0, true, quizId));
        assertEquals(1, (int) LiveDataTestUtil.getValue(mDao.getCorrectCount(quizId, s0)));

        String s1 = "jǐu níu yī máo";
        String w1 = "九牛一毛";
        mDao.insert(new Word(w1, s1));
        mDao.insert(new QuizPinyin(quizId, s1, w1, false));
//        assertEquals(0, (int) LiveDataTestUtil.getValue(mDao.getCorrectCount(quizId, s0)));
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
    public void addSameWordTo2Quizzes() {

    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = ApplicationProvider.getApplicationContext();

        assertEquals("com.zhiyong.tingxie", appContext.getPackageName());
    }

    // This test is obsolete since MIGRATION_3_4.
//    @Test
//    public void pinyin4words5() throws InterruptedException {
//        // Add 4 pinyins with 5 words.
//        String s0 = "jiāo tà shí dì";
//        String s1 = "jiǔ niú yì máo";
//        String s2 = "yí jiàn zhōng qíng";
//        String s3 = "guǐ jì";
//        String w0 = "脚踏实地";
//        String w1 = "九牛一毛";
//        String w2 = "一见钟情";
//        String w3 = "轨迹";
//        String w4 = "诡计";
//        mDao.insert(new Word(w0, s0));
//        mDao.insert(new Word(w1, s1));
//        mDao.insert(new Word(w2, s2));
//        mDao.insert(new Word(w3, s3));
//        mDao.insert(new Word(w4, s3));
//
//        // Connect all pinyin IDs to the test.
//        mDao.insert(new QuizPinyin(quizId, s0, w0));
//        mDao.insert(new QuizPinyin(quizId, s1, w1));
//        mDao.insert(new QuizPinyin(quizId, s2, w2));
//        mDao.insert(new QuizPinyin(quizId, s3, w3));
//
//        QuizItem result = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
//        assertEquals(testDate, result.getDate());
//        assertEquals(4, result.getTotalWords());
//        assertEquals(4, result.getNotLearned());
//        assertEquals(1, result.getRound());
//
//        mDao.deleteQuizPinyin(quizId, s0);
//        result = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
//        assertEquals(3, result.getTotalWords());
//    }
}
