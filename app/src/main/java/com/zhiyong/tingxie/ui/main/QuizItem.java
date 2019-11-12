package com.zhiyong.tingxie.ui.main;

import androidx.room.Ignore;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class QuizItem {
    long id;
    int date;
    String title;
    int totalTerms;
    int notLearned;
    int roundsCompleted;

    @Ignore
    public QuizItem() {}

    public QuizItem(long id, int date, String title, int totalTerms, int notLearned, int roundsCompleted) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.totalTerms = totalTerms;
        this.notLearned = notLearned;
        this.roundsCompleted = roundsCompleted;
    }
}
