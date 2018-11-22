package com.zhiyong.tingxie;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "test_pinyin",
        indices = {@Index("test_id"), @Index("pinyin_id")},
        foreignKeys = {
        @ForeignKey(entity = Test.class, parentColumns = "id", childColumns = "test_id", onDelete = CASCADE),
                @ForeignKey(entity = Pinyin.class, parentColumns = "id", childColumns = "pinyin_id")
})
public class TestPinyin {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private int test_id;
    private int pinyin_id;

    public TestPinyin(@NonNull int test_id, @NonNull int pinyin_id) {
        this.test_id = test_id;
        this.pinyin_id = pinyin_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    @NonNull
    public int getPinyin_id() {
        return pinyin_id;
    }

    public void setPinyin_id(int pinyin_id) {
        this.pinyin_id = pinyin_id;
    }
}
