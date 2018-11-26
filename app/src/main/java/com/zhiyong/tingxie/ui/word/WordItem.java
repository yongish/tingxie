package com.zhiyong.tingxie.ui.word;

public class WordItem {
    private int quizId;
    private String word;
    private String pinyin;

    public WordItem(int quizId, String word, String pinyin) {
        this.quizId = quizId;
        this.word = word;
        this.pinyin = pinyin;
    }

    public int getQuizId() {
        return quizId;
    }

    public String getWord() {
        return word;
    }

    public String getPinyin() {
        return pinyin;
    }
}
