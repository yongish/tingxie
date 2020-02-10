package com.zhiyong.tingxie.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "quiz_pinyin",
        indices = {@Index("quizId"), @Index("pinyinString")},
        foreignKeys = {
        @ForeignKey(entity = Quiz.class, parentColumns = "id", childColumns = "quizId", onDelete = CASCADE),
                @ForeignKey(entity = Pinyin.class, parentColumns = "pinyinString", childColumns = "pinyinString")
})
public class QuizPinyin {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    @ColumnInfo(name = "quiz_id")
    private long quizId;
    @ColumnInfo(name = "pinyin_string")
    private String pinyinString;
    @ColumnInfo(name = "word_string")
    private String wordString;

    public QuizPinyin(@NonNull long quizId, @NonNull String pinyinString) {
        this.quizId = quizId;
        this.pinyinString = pinyinString;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public long getQuizId() {
        return quizId;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }

    @NonNull
    public String getPinyinString() {
        return pinyinString;
    }

    public void setPinyinString(String pinyinString) {
        this.pinyinString = pinyinString;
    }

    @NonNull
    public String getWordString() {
        return wordString;
    }

    public void setWordString(String wordString) {
        this.wordString = wordString;
    }
}
