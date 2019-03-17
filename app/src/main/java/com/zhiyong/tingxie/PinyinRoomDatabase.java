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

@Database(entities = {Question.class, Quiz.class, Pinyin.class, Word.class, QuizPinyin.class},
        version = 1)
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

        PopulateDbAsync(PinyinRoomDatabase db) {
            mDao = db.pinyinDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // todo: Do not delete all.
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
//            mDao.deleteAllQuizzes();
//            mDao.deleteAllQuizPinyins();
//            mDao.deleteAllWords();
//            mDao.deleteAllPinyins();
//            mDao.deleteAllQuestions();

            // Add a quiz.
//            long quizId = mDao.insert(new Quiz(20181201));
//            Map<String, String> examples = new HashMap<>();
//            examples.put("脚踏实地", "jiāo dà shí dì");
//            examples.put("九牛一毛", "jǐu níu yī máo");
//            examples.put("一见钟情", "yí jiàn zhōng qíng");
//            examples.put("轨迹", "guǐ jì");
//            examples.put("诡计", "guǐ jì");
//            examples.put("了不起", "liǎo bù qǐ");
//            examples.put("破釜成舟", "pò fǔ chén zhōu");
//            examples.put("指鹿为马", "zhǐ lù wéi mǎ");
//            examples.put("乐不思蜀", "lè bù sī shǔ");
//            examples.put("朝三暮四", "zhǎo sān mù sì");
//            examples.put("井底之蛙", "jǐng dǐ zhī wā");

//            for (Map.Entry<String, String> entry : examples.entrySet()) {
//                String wordString = entry.getKey();
//                String pinyinString = entry.getValue();
//                mDao.insert(new Pinyin(pinyinString));
//                mDao.insert(new Word(wordString, pinyinString));
//                // Connect all pinyin IDs to the test.
//                mDao.insert(new QuizPinyin(quizId, pinyinString));
//            }

//            for (int quizDate = 20180201; quizDate < 20181102; quizDate += 100) {
//                mDao.insert(new Quiz(quizDate));
//            }
//
//            for (int quizDate = 20190101; quizDate < 20191202; quizDate += 100) {
//                mDao.insert(new Quiz(quizDate));
//            }

            return null;
        }
    }
}
