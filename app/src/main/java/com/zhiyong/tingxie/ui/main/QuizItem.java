package com.zhiyong.tingxie.ui.main;

public class QuizItem {
    private long id;
    private int date;
    private int totalWords;
    private int notLearned;
    private int round;

    public QuizItem(long id, int date, int totalWords, int notLearned, int round) {
        this.id = id;
        this.date = date;
        this.totalWords = totalWords;
        this.notLearned = notLearned;
        this.round = round;
    }

    public long getId() {
        return id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
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
