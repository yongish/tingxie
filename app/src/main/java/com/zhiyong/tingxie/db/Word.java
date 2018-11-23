package com.zhiyong.tingxie.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("pinyin_id")},
        foreignKeys = @ForeignKey(entity = Pinyin.class, parentColumns = "id", childColumns = "pinyin_id"))
public class Word {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    private String word;

    private long pinyin_id;

    public Word(@NonNull String word, @NonNull long pinyin_id) {
        this.word = word;
        this.pinyin_id = pinyin_id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    @NonNull
    public long getId() {
        return id;
    }

    @NonNull
    public String getWord() {
        return word;
    }

    @NonNull
    public long getPinyin_id() {
        return pinyin_id;
    }

    // Word instances should be immutable.
}
