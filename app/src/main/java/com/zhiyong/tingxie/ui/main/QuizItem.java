package com.zhiyong.tingxie.ui.main;

public class QuizItem {
    private long id;
    private int date;
    private String title;
    private int totalTerms;
    private int notLearned;
    private int roundsCompleted;

    public QuizItem(long id, int date, String title, int totalTerms, int notLearned, int roundsCompleted) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.totalTerms = totalTerms;
        this.notLearned = notLearned;
        this.roundsCompleted = roundsCompleted;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalTerms() {
        return totalTerms;
    }

    public int getNotLearned() {
        return notLearned;
    }

    public int getRoundsCompleted() {
        return roundsCompleted;
    }
}
