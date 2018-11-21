package com.example.android;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public class Word {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String word;

    public Word(@NonNull String word) {
        this.word = word;
    }

    @NonNull
    public int getId() {
        return id;
    }

    @NonNull
    public String getWord() {
        return word;
    }
}
