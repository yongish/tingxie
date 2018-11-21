package com.example.android;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public class Pinyin {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String pinyin;

    public Pinyin(@NonNull String pinyin) {
        this.pinyin = pinyin;
    }

    @NonNull
    public int getId() {
        return id;
    }

    @NonNull
    public String getPinyin() {
        return pinyin;
    }
}
