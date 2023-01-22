package com.zhiyong.tingxie.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "quiz_pinyin", indices = {@Index("quiz_id"), @Index("pinyin_string")})
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
    @NonNull
    private boolean asked;

    public QuizPinyin(long quizId, @NonNull String pinyinString,
                      @NonNull String wordString, @NonNull boolean asked) {
        this.quizId = quizId;
        this.pinyinString = pinyinString;
        this.wordString = wordString;
        this.asked = asked;
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

    public boolean isAsked() {
        return asked;
    }

    public void setAsked(boolean asked) {
        this.asked = asked;
    }
}
