package com.example.android;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Test_Pinyin.class,
        parentColumns = "pinyin_id", childColumns = "pinyin_id"))
public class Question {
    @PrimaryKey
    @NonNull
    private int id;

    private long timestamp;

    private int pinyin_id;

    private boolean correct;

    public Question(@NonNull long timestamp, @NonNull int pinyin_id, @NonNull boolean correct) {
        this.timestamp = timestamp;
        this.pinyin_id = pinyin_id;
        this.correct = correct;
    }

    @NonNull
    public int getId() {
        return id;
    }

    @NonNull
    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    public int getPinyin_id() {
        return pinyin_id;
    }

    @NonNull
    public boolean isCorrect() {
        return correct;
    }
}
