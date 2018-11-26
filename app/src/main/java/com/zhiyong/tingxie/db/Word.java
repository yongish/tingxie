package com.zhiyong.tingxie.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("pinyin")},
        foreignKeys = @ForeignKey(entity = Pinyin.class, parentColumns = "pinyin", childColumns = "pinyin"))
public class Word {
    @PrimaryKey
    @NonNull
    private String word;

    private String pinyin;

    public Word(@NonNull String word, @NonNull String pinyin) {
        this.word = word;
        this.pinyin = pinyin;
    }

    @NonNull
    public String getWord() {
        return word;
    }

    @NonNull
    public String getPinyin() {
        return pinyin;
    }

    // Word instances should be immutable.
}
