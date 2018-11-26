package com.zhiyong.tingxie.ui.word;

public class WordItem {
    private int quizId;
    private String wordString;
    private String pinyin;

    public WordItem(int quizId, String wordString, String pinyin) {
        this.quizId = quizId;
        this.wordString = wordString;
        this.pinyin = pinyin;
    }

    public int getQuizId() {
        return quizId;
    }

    public String getWordString() {
        return wordString;
    }

    public String getPinyin() {
        return pinyin;
    }
}
