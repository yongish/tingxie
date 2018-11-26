package com.zhiyong.tingxie.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("pinyin_string")},
        foreignKeys = @ForeignKey(entity = Pinyin.class, parentColumns = "pinyin_string", childColumns = "pinyin_string"))
public class Word {
    @PrimaryKey
    @NonNull
    private String word_string;

    private String pinyin_string;

    public Word(@NonNull String word_string, @NonNull String pinyin_string) {
        this.word_string = word_string;
        this.pinyin_string = pinyin_string;
    }

    @NonNull
    public String getWord_string() {
        return word_string;
    }

    @NonNull
    public String getPinyin_string() {
        return pinyin_string;
    }

    // Word instances should be immutable.
}
