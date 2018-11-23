package com.zhiyong.tingxie;

import java.util.Date;

public class QuizItem {
//    private Date date;
    private int date;
    private int totalWords;
    private int notLearned;
    private int round;

    public QuizItem(int date, int totalWords, int notLearned, int round) {
        this.date = date;
        this.totalWords = totalWords;
        this.notLearned = notLearned;
        this.round = round;
    }

//    public Date getDate() {
//        return date;
//    }
    public int getDate() {
        return date;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public int getNotLearned() {
        return notLearned;
    }

    public int getRound() {
        return round;
    }
}
