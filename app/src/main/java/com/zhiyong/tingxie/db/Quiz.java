package com.zhiyong.tingxie.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(indices = {@Index("id")})
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    private int date;
    private String title;
    @ColumnInfo(name = "total_words")
    private int totalWords; // todo: need to update these values.
    @ColumnInfo(name = "not_learned")
    private int notLearned;
    private int round;

    public Quiz(@NonNull Integer date) {
        this.date = date;
        title = "No title";
        totalWords = 0;
        notLearned = 0;
        round = 1;
    }

    @Ignore
    public Quiz(long id, @NonNull Integer date, String title,
                int totalWords, int notLearned, int round) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.totalWords = totalWords;
        this.notLearned = notLearned;
        this.round = round;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    public Integer getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    public Integer getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    @NonNull
    public Integer getNotLearned() {
        return notLearned;
    }

    public void setNotLearned(int notLearned) {
        this.notLearned = notLearned;
    }

    @NonNull
    public Integer getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
