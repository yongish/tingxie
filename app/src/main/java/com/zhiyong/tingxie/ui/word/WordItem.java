package com.zhiyong.tingxie.ui.word;

public class WordItem {
    private int quizId;
    private String wordString;
    private String pinyinString;

    public WordItem(int quizId, String wordString, String pinyinString) {
        this.quizId = quizId;
        this.wordString = wordString;
        this.pinyinString = pinyinString;
    }

    public int getQuizId() {
        return quizId;
    }

    public String getWordString() {
        return wordString;
    }

    public String getPinyinString() {
        return pinyinString;
    }
}
