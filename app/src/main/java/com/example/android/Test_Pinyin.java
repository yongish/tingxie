package com.example.android;

import android.support.annotation.NonNull;

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
