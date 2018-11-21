package com.example.android;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = {
        @ForeignKey(entity = Test.class, parentColumns = "id", childColumns = "test_id"),
        @ForeignKey(entity = Pinyin.class, parentColumns = "id", childColumns = "pinyin_id")
})
public class Test_Pinyin {
    private int test_id;
    private int pinyin_id;

    public Test_Pinyin(@NonNull int test_id, @NonNull int pinyin_id) {
        this.test_id = test_id;
        this.pinyin_id = pinyin_id;
    }

    @NonNull
    public int getTest_id() {
        return test_id;
    }

    @NonNull
    public int getPinyin_id() {
        return pinyin_id;
    }
}
