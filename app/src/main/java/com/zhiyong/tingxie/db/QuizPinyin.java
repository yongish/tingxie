package com.zhiyong.tingxie.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "quiz_pinyin",
        indices = {@Index("quiz_id"), @Index("pinyin_string")},
        foreignKeys = {
                @ForeignKey(entity = Quiz.class, parentColumns = "id", childColumns = "quiz_id",
                        onDelete = CASCADE)
        })
public class QuizPinyin {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "quiz_id")
    @NonNull
    private Long quizId;
    @ColumnInfo(name = "pinyin_string")
    @NonNull
    private String pinyinString;
    @ColumnInfo(name = "word_string")
    @NonNull
    private String wordString;

    public QuizPinyin(long quizId, @NonNull String pinyinString, @NonNull String wordString) {
        this.quizId = quizId;
        this.pinyinString = pinyinString;
        this.wordString = wordString;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public Long getQuizId() {
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
