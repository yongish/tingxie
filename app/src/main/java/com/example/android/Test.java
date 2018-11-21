package com.example.android;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public class Test {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;

    public Test(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public int getId() {
        return id;
    }

    @NonNull
    public String getDate() {
        return date;
    }
}
