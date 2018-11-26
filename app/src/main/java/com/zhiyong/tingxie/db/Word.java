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
    private String word_string;

    private String pinyin;

    public Word(@NonNull String word_string, @NonNull String pinyin) {
        this.word_string = word_string;
        this.pinyin = pinyin;
    }

    @NonNull
    public String getWord_string() {
        return word_string;
    }

    @NonNull
    public String getPinyin() {
        return pinyin;
    }

    // Word instances should be immutable.
}
