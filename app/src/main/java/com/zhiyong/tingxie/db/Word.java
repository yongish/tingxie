package com.zhiyong.tingxie.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(indices = {@Index("pinyinString")},
        foreignKeys = @ForeignKey(entity = Pinyin.class, parentColumns = "pinyinString", childColumns = "pinyinString"))
public class Word {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word_string")
    private String wordString;
    @ColumnInfo(name = "pinyin_string")
    private String pinyinString;

    public Word(@NonNull String wordString, @NonNull String pinyinString) {
        this.wordString = wordString;
        this.pinyinString = pinyinString;
    }

    @NonNull
    public String getWordString() {
        return wordString;
    }

    @NonNull
    public String getPinyinString() {
        return pinyinString;
    }

    // Word instances should be immutable.
}
