package com.example.android;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("id")})
public class Test {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;

    public Test(@NonNull String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
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
