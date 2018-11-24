package com.zhiyong.tingxie;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyong.tingxie.db.Pinyin;
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
@RunWith(AndroidJUnit4.class)
public class DBInstrumentedTest {
    private QuizDao mDao;
    private PinyinRoomDatabase mDb;

    @Before
    public void createDb() {
        mDb = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), PinyinRoomDatabase.class).build();
        mDao = mDb.pinyinDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void blankTest() throws InterruptedException {
        // Add a quiz.
        final int testDate = 20190101;
        long quizId = mDao.insert(new Quiz(testDate));

        QuizItem result = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(testDate, result.getDate());
        assertEquals(0, result.getTotalWords());
        assertEquals(0, result.getNotLearned());
        assertEquals(1, result.getRound());
    }

    @Test
    public void pinyin4words5() throws InterruptedException {
        // Add a quiz.
        final int testDate = 20190101;
        long quizId = mDao.insert(new Quiz(testDate));

        // Add 4 pinyins with 5 words.
        long p0Id = mDao.insert(new Pinyin("p0"));
        long p1Id = mDao.insert(new Pinyin("p1"));
        long p2Id = mDao.insert(new Pinyin("p2"));
        long p3Id = mDao.insert(new Pinyin("p3"));
        mDao.insert(new Word("w0", p0Id));
        mDao.insert(new Word("w1", p1Id));
        mDao.insert(new Word("w2", p2Id));
        mDao.insert(new Word("w3", p3Id));
        mDao.insert(new Word("w4", p3Id));

        // Connect all pinyin IDs to the test.
        mDao.insert(new QuizPinyin(quizId, p0Id));
        mDao.insert(new QuizPinyin(quizId, p1Id));
        mDao.insert(new QuizPinyin(quizId, p2Id));
        mDao.insert(new QuizPinyin(quizId, p3Id));

        QuizItem result = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(testDate, result.getDate());
        assertEquals(4, result.getTotalWords());
        assertEquals(4, result.getNotLearned());
        assertEquals(1, result.getRound());

        mDao.deleteQuizPinyin(quizId, p0Id);
        result = LiveDataTestUtil.getValue(mDao.getAllQuizItems()).get(0);
        assertEquals(3, result.getTotalWords());
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.zhiyong.tingxie", appContext.getPackageName());
    }

}
