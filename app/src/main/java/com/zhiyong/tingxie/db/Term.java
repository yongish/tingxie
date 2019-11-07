/**
 * todo: May delete this because we do not directly access MySQL DB.
 */
package com.zhiyong.tingxie.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(indices = {@Index("id")})
public class Term {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    private long createdAt;

    private long deletedAt;

    private long quizId;

    private String uid;

    private String word;

    private String pinyin;
    private int timesCorrect;

    public Term(@NonNull long createdAt, @NonNull long deletedAt, @NonNull long quizId,
                @NonNull String uid, @NonNull String word, @NonNull String pinyin) {
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.quizId = quizId;
        this.uid = uid;
        this.word = word;
        this.pinyin = pinyin;
    }

    @Ignore
    public Term(long id, long createdAt, long deletedAt, long quizId, String uid, String word, String pinyin, int timesCorrect) {
        this.id = id;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.quizId = quizId;
        this.uid = uid;
        this.word = word;
        this.pinyin = pinyin;
        this.timesCorrect = timesCorrect;
    }

    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(long deletedAt) {
        this.deletedAt = deletedAt;
    }

    public long getQuizId() {
        return quizId;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getTimesCorrect() {
        return timesCorrect;
    }

    public void setTimesCorrect(int timesCorrect) {
        this.timesCorrect = timesCorrect;
    }
}
