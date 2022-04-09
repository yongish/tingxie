package com.zhiyong.tingxie

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zhiyong.tingxie.db.*

@Database(entities =
[Question::class, Quiz::class, Word::class, QuizPinyin::class, QuizRole::class],
        version = 9)
abstract class PinyinRoomDatabase : RoomDatabase() {
    fun pinyinDao(): QuizDao {
        return pinyinDao
    }

    abstract val pinyinDao: QuizDao
}

private lateinit var INSTANCE: PinyinRoomDatabase

fun getDatabase(context: Context): PinyinRoomDatabase {

    synchronized(PinyinRoomDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    PinyinRoomDatabase::class.java, "pinyin_database")
                    .addMigrations(
                        MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5,
                        MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9
                    )
                    .build()
        }
    }
    return INSTANCE
}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE question ADD COLUMN reset_time INTEGER NOT NULL DEFAULT 1558504709")
    }
}
val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE quiz ADD COLUMN title TEXT DEFAULT 'No title'")
    }
}
val MIGRATION_3_4: Migration = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE quiz ADD COLUMN total_words INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE quiz ADD COLUMN not_learned INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE quiz ADD COLUMN round INTEGER NOT NULL DEFAULT 0")

        database.execSQL("""
            CREATE TABLE temp_pinyin_correct AS
            SELECT quiz.id AS id, title, tp.pinyin_string, date, Count(correct) AS correct_count
            FROM quiz
            LEFT JOIN quiz_pinyin tp ON quiz.id = tp.quiz_id
            LEFT JOIN question q ON tp.quiz_id = q.quiz_id
            AND tp.pinyin_string = q.pinyin_string
            AND q.reset_time <= q.timestamp
            GROUP BY quiz.id, title, tp.pinyin_string, date
        """)
        database.execSQL("""
            CREATE TABLE temp_quiz_stats AS
            SELECT tpc.id, Count(tpc.pinyin_string) AS total_words, Min(correct_count) AS rounds_completed
            FROM temp_pinyin_correct tpc
            GROUP BY tpc.id
        """)

        database.execSQL("""
            CREATE TABLE temp_quiz AS
            SELECT tpc.id, 
                total_words, 
                CASE WHEN Min(tp2.total_words) < Count(tp2.rounds_completed = tpc.correct_count)
                    THEN Min(tp2.total_words) 
                    ELSE Count(tp2.rounds_completed = tpc.correct_count)
                    END AS not_learned,
                tp2.rounds_completed + 1 AS round
            FROM temp_pinyin_correct tpc LEFT JOIN temp_quiz_stats tp2 ON tp2.id = tpc.id
            GROUP BY tpc.id, total_words, rounds_completed
            """)

        database.execSQL("""
        UPDATE quiz
        SET total_words = (SELECT total_words FROM temp_quiz qa WHERE id = qa.id),
            not_learned = (SELECT not_learned FROM temp_quiz qa WHERE id = qa.id),
            round = (SELECT round FROM temp_quiz qa WHERE id = qa.id)
        WHERE id = (SELECT id FROM temp_quiz qa WHERE id = qa.id)
        """)
        database.execSQL("DROP TABLE temp_pinyin_correct")
        database.execSQL("DROP TABLE temp_quiz_stats")
        database.execSQL("DROP TABLE temp_quiz")

        database.execSQL("DROP TABLE IF EXISTS pinyin")
        database.execSQL("CREATE TABLE quiz_pinyin_temp (id INTEGER NOT NULL PRIMARY KEY, quiz_id INTEGER NOT NULL, pinyin_string TEXT NOT NULL, word_string TEXT NOT NULL, CONSTRAINT fk_quiz FOREIGN KEY (quiz_id) REFERENCES Quiz(id) ON DELETE CASCADE)")
        database.execSQL("INSERT INTO quiz_pinyin_temp SELECT id, quiz_id, pinyin_string, \"\" FROM quiz_pinyin")
        database.execSQL("""
            UPDATE quiz_pinyin_temp
            SET word_string = (
                SELECT word_string FROM word 
                WHERE word.pinyin_string = quiz_pinyin_temp.pinyin_string
            )
            """)
        database.execSQL("DROP TABLE quiz_pinyin")
        database.execSQL("ALTER TABLE quiz_pinyin_temp RENAME TO quiz_pinyin")
        database.execSQL("CREATE INDEX index_quiz_pinyin_quiz_id ON quiz_pinyin(quiz_id)")
        database.execSQL("CREATE INDEX index_quiz_pinyin_pinyin_string ON quiz_pinyin(pinyin_string)")
        database.execSQL("CREATE TABLE word_temp (word_string TEXT NOT NULL PRIMARY KEY, pinyin_string TEXT)")
        database.execSQL("INSERT INTO word_temp SELECT word_string, pinyin_string FROM word")
        database.execSQL("DROP TABLE word")
        database.execSQL("ALTER TABLE word_temp RENAME TO word")
        database.execSQL("CREATE INDEX index_Word_pinyin_string ON word(pinyin_string)")
    }
}
val MIGRATION_4_5: Migration = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE quiz_pinyin ADD COLUMN asked INTEGER NOT NULL DEFAULT 0")
    }
}
val MIGRATION_5_6: Migration = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE quiz_pinyin_temp (id INTEGER NOT NULL PRIMARY KEY, quiz_id INTEGER NOT NULL, pinyin_string TEXT NOT NULL, word_string TEXT NOT NULL, asked INTEGER NOT NULL)")
        database.execSQL("INSERT INTO quiz_pinyin_temp (id, quiz_id, pinyin_string, word_string, asked) SELECT id, quiz_id, pinyin_string, word_string, asked FROM quiz_pinyin")
        database.execSQL("DROP TABLE quiz_pinyin")
        database.execSQL("ALTER TABLE quiz_pinyin_temp RENAME TO quiz_pinyin")
        database.execSQL("CREATE INDEX index_quiz_pinyin_quiz_id ON quiz_pinyin(quiz_id)")
        database.execSQL("CREATE INDEX index_quiz_pinyin_pinyin_string ON quiz_pinyin(pinyin_string)")
    }
}
val MIGRATION_6_7: Migration = object : Migration(6, 7) {
  override fun migrate(database: SupportSQLiteDatabase) {
    database.execSQL("CREATE TABLE quiz_temp (id INTEGER NOT NULL PRIMARY KEY, date INTEGER NOT NULL, title TEXT NOT NULL, total_words INTEGER NOT NULL, not_learned INTEGER NOT NULL, round INTEGER NOT NULL, synced INTEGER NOT NULL, status INTEGER NOT NULL)")
    database.execSQL("INSERT INTO quiz_temp SELECT id, date, title, total_words, not_learned, round, 0, 0 FROM quiz")
    database.execSQL("DROP TABLE quiz")
    database.execSQL("ALTER TABLE quiz_temp RENAME TO quiz")
    database.execSQL("CREATE INDEX index_Quiz_id ON quiz(id)")
  }
}
val MIGRATION_7_8: Migration = object : Migration(7, 8) {
  override fun migrate(database: SupportSQLiteDatabase) {
    database.execSQL("CREATE TABLE quiz_role (id INTEGER PRIMARY KEY, quiz_id INTEGER NOT NULL, email TEXT NOT NULL, role TEXT NOT NULL)")
  }
}
val MIGRATION_8_9: Migration = object : Migration(8, 9) {
  override fun migrate(database: SupportSQLiteDatabase) {
    database.execSQL("ALTER TABLE quiz ADD COLUMN status TEXT NOT NULL DEFAULT 'NOT_DELETED'")
    database.execSQL("ALTER TABLE quiz_pinyin ADD COLUMN status TEXT NOT NULL DEFAULT 'NOT_DELETED'")
  }
}
