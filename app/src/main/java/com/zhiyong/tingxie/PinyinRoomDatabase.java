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

    public static PinyinRoomDatabase getDatabase(final Context context) {
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

            mDao.insert(new Quiz(20190201));

            return null;
        }
    }
}
