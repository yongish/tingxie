package com.zhiyong.tingxie.db

import androidx.annotation.NonNull
import androidx.room.*

@Entity(
    tableName = "quiz_pinyin",
    indices = [Index("quiz_id"), Index("pinyin_string")]
)
data class QuizPinyin(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "quiz_id") @NonNull val quizId: Long,
    @ColumnInfo(name = "pinyin_string") @NonNull val pinyinString: String,
    @ColumnInfo(name = "word_string") @NonNull val wordString: String,
    @NonNull val asked: Boolean,
    @NonNull val status: String = "NOT_DELETED",
    ) {
    @Ignore
    constructor(quizId: Long, pinyinString: String, wordString: String, asked: Boolean):
        this(0, quizId, pinyinString, wordString, asked)
}
