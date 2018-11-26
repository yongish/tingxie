package com.zhiyong.tingxie.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Pinyin {
    @PrimaryKey
    @NonNull
    private String pinyin;

    public Pinyin(@NonNull String pinyin) {
        this.pinyin = pinyin;
    }

    @NonNull
    public String getPinyin() {
        return pinyin;
    }

    // Pinyin instances should be immutable.
}
