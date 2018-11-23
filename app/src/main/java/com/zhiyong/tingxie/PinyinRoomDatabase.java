package com.zhiyong.tingxie;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.zhiyong.tingxie.db.Pinyin;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.db.Word;

// todo: exportSchema should be changed to true after 1st release.
@Database(entities = {Question.class, Quiz.class, Pinyin.class, Word.class, QuizPinyin.class},
        version = 1, exportSchema = false)
public abstract class PinyinRoomDatabase extends RoomDatabase {
    public abstract QuizDao pinyinDao();
    private static PinyinRoomDatabase INSTANCE;

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    static PinyinRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PinyinRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PinyinRoomDatabase.class, "pinyin_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Populate the database in the background.
     */
    static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final QuizDao mDao;
        int[] dates = {20190103, 20190201, 20190221, 20190301, 20190502};

        PopulateDbAsync(PinyinRoomDatabase db) {
            mDao = db.pinyinDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // todo: Do not delete all.
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            mDao.deleteAllQuizzes();
            mDao.deleteAllQuizPinyins();
            mDao.deleteAllWords();
            mDao.deleteAllPinyins();
            mDao.deleteAllQuestions();

            // Add a quiz.
            long quizId = mDao.insert(new Quiz(20190101));

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

            return null;
        }
    }
}
