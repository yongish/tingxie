package com.zhiyong.tingxie.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("id")})
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private long id;

    private int date;

    private String title;

    public Quiz(@NonNull int date) {
        this.date = date;
        title = "No title";
    }

    @Ignore
    public Quiz(long id, @NonNull int date, String title) {
        this.id = id;
        this.date = date;
        this.title = title;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    public int getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }
}
