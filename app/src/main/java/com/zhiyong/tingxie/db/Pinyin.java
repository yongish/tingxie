package com.zhiyong.tingxie.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class Pinyin {
    @PrimaryKey
    @NonNull
    private String pinyin_string;

    public Pinyin(@NonNull String pinyin_string) {
        this.pinyin_string = pinyin_string;
    }

    @NonNull
    public String getPinyin_string() {
        return pinyin_string;
    }

    // Pinyin instances should be immutable.
}
