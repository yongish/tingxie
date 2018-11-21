package com.example.android;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Pinyin.class, parentColumns = "id", childColumns = "pinyin_id"))
public class Word {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String word;

    private int pinyin_id;

    public Word(@NonNull String word, @NonNull int pinyin_id) {
        this.word = word;
        this.pinyin_id = pinyin_id;
    }

    @NonNull
    public int getId() {
        return id;
    }

    @NonNull
    public String getWord() {
        return word;
    }

    @NonNull
    public int getPinyin_id() {
        return pinyin_id;
    }

}
