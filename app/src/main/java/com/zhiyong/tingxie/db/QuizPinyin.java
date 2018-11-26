package com.zhiyong.tingxie.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "quiz_pinyin",
        indices = {@Index("quiz_id"), @Index("pinyin_string")},
        foreignKeys = {
        @ForeignKey(entity = Quiz.class, parentColumns = "id", childColumns = "quiz_id", onDelete = CASCADE),
                @ForeignKey(entity = Pinyin.class, parentColumns = "pinyin_string", childColumns = "pinyin_string")
})
public class QuizPinyin {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    private long quiz_id;
    private String pinyin_string;

    public QuizPinyin(@NonNull long quiz_id, @NonNull String pinyin_string) {
        this.quiz_id = quiz_id;
        this.pinyin_string = pinyin_string;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public long getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(long quiz_id) {
        this.quiz_id = quiz_id;
    }

    @NonNull
    public String getPinyin_string() {
        return pinyin_string;
    }

    public void setPinyin_string(String pinyin_string) {
        this.pinyin_string = pinyin_string;
    }
}
