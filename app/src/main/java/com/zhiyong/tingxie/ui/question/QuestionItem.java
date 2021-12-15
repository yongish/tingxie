package com.zhiyong.tingxie.ui.question;

public class QuestionItem {
    private final String wordString;
    private final String pinyinString;
    private final boolean correct;

    public QuestionItem(String wordString, String pinyinString, boolean correct) {
        this.wordString = wordString;
        this.pinyinString = pinyinString;
        this.correct = correct;
    }

    public String getWordString() {
        return wordString;
    }

    public String getPinyinString() {
        return pinyinString;
    }

    public boolean isCorrect() {
        return correct;
    }
}
