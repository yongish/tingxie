package com.zhiyong.tingxie.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("id")})
public class Pinyin {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    private String pinyin;

    public Pinyin(@NonNull String pinyin) {
        this.pinyin = pinyin;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    @NonNull
    public long getId() {
        return id;
    }

    @NonNull
    public String getPinyin() {
        return pinyin;
    }

    // Pinyin instances should be immutable.
}
