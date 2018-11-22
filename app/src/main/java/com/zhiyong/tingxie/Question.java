package com.zhiyong.tingxie;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("test_id")}, foreignKeys = @ForeignKey(entity = Test.class, parentColumns = "id",
        childColumns = "test_id", onDelete = CASCADE))
public class Question {
    @PrimaryKey
    @NonNull
    private int id;

    private long timestamp;

    private int pinyin_id;

    private boolean correct;

    private int test_id;

    public Question(@NonNull long timestamp, @NonNull int pinyin_id, @NonNull boolean correct,
                    @NonNull int test_id) {
        this.timestamp = timestamp;
        this.pinyin_id = pinyin_id;
        this.correct = correct;
        this.test_id = test_id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
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

    @NonNull
    public int getTest_id() {
        return test_id;
    }

}
