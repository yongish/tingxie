package com.zhiyong.tingxie;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.zhiyong.tingxie.db.Question;
import com.zhiyong.tingxie.db.Quiz;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.db.Word;

@Database(entities = {Question.class, Quiz.class, Word.class, QuizPinyin.class},
        version = 4)
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
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE question ADD COLUMN reset_time INTEGER NOT NULL DEFAULT 1558504709");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE quiz ADD COLUMN title TEXT DEFAULT 'No title'");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE quiz ADD COLUMN total_words INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE quiz ADD COLUMN not_learned INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE quiz ADD COLUMN round INTEGER NOT NULL DEFAULT 0");
            database.execSQL("WITH qa(id, total_words, not_learned, round) AS\n" +
                    "  (WITH tpc AS\n" +
                    "     (SELECT quiz.id AS id,\n" +
                    "             title,\n" +
                    "             tp.pinyin_string, date, Count(correct) AS correct_count\n" +
                    "      FROM quiz\n" +
                    "      LEFT JOIN quiz_pinyin tp ON quiz.id = tp.quiz_id\n" +
                    "      LEFT JOIN question q ON tp.quiz_id = q.quiz_id\n" +
                    "      AND tp.pinyin_string = q.pinyin_string\n" +
                    "      AND q.reset_time <= q.timestamp\n" +
                    "      GROUP BY quiz.id,\n" +
                    "               title,\n" +
                    "               tp.pinyin_string, date),\n" +
                    "        tp2 AS\n" +
                    "     (SELECT tpc.id,\n" +
                    "             Count(tpc.pinyin_string) AS total,\n" +
                    "             Min(correct_count) AS rounds_completed\n" +
                    "      FROM tpc\n" +
                    "      GROUP BY tpc.id) SELECT tpc.id,\n" +
                    "                              tp2.total AS total_words,\n" +
                    "                              Min(tp2.total, Count(tp2.rounds_completed = tpc.correct_count)) AS not_learned,\n" +
                    "                              tp2.rounds_completed + 1 AS round\n" +
                    "   FROM tpc\n" +
                    "   LEFT JOIN tp2 ON tp2.id = tpc.id\n" +
                    "   GROUP BY tpc.id,\n" +
                    "            total,\n" +
                    "            rounds_completed)\n" +
                    "UPDATE quiz\n" +
                    "SET total_words =\n" +
                    "  (SELECT total_words\n" +
                    "   FROM qa),\n" +
                    "    not_learned =\n" +
                    "  (SELECT not_learned\n" +
                    "   FROM qa),\n" +
                    "    round =\n" +
                    "  (SELECT round\n" +
                    "   FROM qa)\n" +
                    "WHERE id = qa.id");
            database.execSQL("DROP TABLE IF EXISTS pinyin");

            database.execSQL("CREATE TABLE quiz_pinyin_temp (id INTEGER NOT NULL PRIMARY KEY, quiz_id INTEGER NOT NULL, pinyin_string TEXT NOT NULL, word_string TEXT NOT NULL, CONSTRAINT fk_quiz FOREIGN KEY (quiz_id) REFERENCES Quiz(id) ON DELETE CASCADE)");
            database.execSQL("INSERT INTO quiz_pinyin_temp SELECT id, quiz_id, pinyin_string, \"\" FROM quiz_pinyin");
            database.execSQL("\n" +
                    "UPDATE quiz_pinyin_temp\n" +
                    "SET word_string =\n" +
                    "  (SELECT word_string\n" +
                    "   FROM word\n" +
                    "   WHERE word.pinyin_string = quiz_pinyin_temp.pinyin_string)");
            database.execSQL("DROP TABLE quiz_pinyin");
            database.execSQL("ALTER TABLE quiz_pinyin_temp RENAME TO quiz_pinyin");
            database.execSQL("CREATE INDEX index_quiz_pinyin_quiz_id ON quiz_pinyin(quiz_id)");
            database.execSQL("CREATE INDEX index_quiz_pinyin_pinyin_string ON quiz_pinyin(pinyin_string)");

            database.execSQL("CREATE TABLE word_temp (word_string TEXT NOT NULL PRIMARY KEY, pinyin_string TEXT)");
            database.execSQL("INSERT INTO word_temp SELECT word_string, pinyin_string FROM word");
            database.execSQL("DROP TABLE word");
            database.execSQL("ALTER TABLE word_temp RENAME TO word");
            database.execSQL("CREATE INDEX index_Word_pinyin_string ON word(pinyin_string)");
        }
    };

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
