package com.zhiyong.tingxie;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zhiyong.tingxie.db.Pinyin;
import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.db.Word;

import java.nio.charset.Charset;
import java.util.Random;

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
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
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

            for (int date : dates) {
                Quiz quiz = new Quiz(date);
                long quizId = mDao.insert(quiz);

                byte[] array = new byte[7]; // length is bounded by 7
                new Random().nextBytes(array);
                String generatedString = new String(array, Charset.forName("UTF-8"));
                Pinyin pinyin = new Pinyin(generatedString);
                long pinyinId = mDao.insert(pinyin);

                // Insert quizPinyin with this quiz ID and pinyin ID.
                Log.d("DATAAAAAAAAAAAAAAAAAAAA", String.valueOf(quizId));
                QuizPinyin quizPinyin = new QuizPinyin(quizId, pinyinId);
                long qpId = mDao.insert(quizPinyin);

                Word word = new Word(generatedString, pinyinId);
                mDao.insert(word);

                Question question = new Question(
                        new Random().nextInt(),
                        pinyinId,
                        new Random().nextBoolean(),
                        quizId
                );
                mDao.insert(question);

            }

            // Insert questions.

            return null;
        }
    }
}
