package com.zhiyong.tingxie.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("quiz_id")}, foreignKeys = @ForeignKey(entity = Quiz.class, parentColumns = "id",
        childColumns = "quiz_id", onDelete = CASCADE))
public class Question {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    private long timestamp;

    private long pinyin_id;

    private boolean correct;

    private long quiz_id;

    public Question(@NonNull long timestamp, @NonNull long pinyin_id, @NonNull boolean correct,
                    @NonNull long quiz_id) {
        this.timestamp = timestamp;
        this.pinyin_id = pinyin_id;
        this.correct = correct;
        this.quiz_id = quiz_id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    @NonNull
    public long getId() {
        return id;
    }

    @NonNull
    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    public long getPinyin_id() {
        return pinyin_id;
    }

    @NonNull
    public boolean isCorrect() {
        return correct;
    }

    @NonNull
    public long getQuiz_id() {
        return quiz_id;
    }

}
