package com.zhiyong.tingxie;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("id")})
public class Test {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    private int date;

    public Test(@NonNull int date) {
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
    public int getDate() {
        return date;
    }
}
