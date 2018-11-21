package com.example.android;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Word.class,
        parentColumns = "id", childColumns = "word_id"))
public class Pinyin {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private int word_id;

    private String pinyin;

    public Pinyin(@NonNull int word_id, @NonNull String pinyin) {
        this.word_id = word_id;
        this.pinyin = pinyin;
    }

    @NonNull
    public int getId() {
        return id;
    }

    @NonNull
    public int getWord_id() {
        return word_id;
    }

    @NonNull
    public String getPinyin() {
        return pinyin;
    }
}
