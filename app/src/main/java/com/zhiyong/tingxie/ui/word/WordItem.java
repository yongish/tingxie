package com.zhiyong.tingxie.ui.word;

public class WordItem {
    private long quizId;
    private String wordString;
    private String pinyinString;

    public WordItem(long quizId, String wordString, String pinyinString) {
        this.quizId = quizId;
        this.wordString = wordString;
        this.pinyinString = pinyinString;
    }

    public long getQuizId() {
        return quizId;
    }

    public String getWordString() {
        return wordString;
    }

    public String getPinyinString() {
        return pinyinString;
    }
}
