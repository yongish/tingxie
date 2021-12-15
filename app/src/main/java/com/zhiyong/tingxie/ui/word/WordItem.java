package com.zhiyong.tingxie.ui.word;

public class WordItem {
    private final long quizId;
    private final String wordString;
    private final String pinyinString;
    private final boolean asked;

    public WordItem(long quizId, String wordString, String pinyinString, boolean asked) {
        this.quizId = quizId;
        this.wordString = wordString;
        this.pinyinString = pinyinString;
        this.asked = asked;
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

    public boolean isAsked() {
        return asked;
    }
}
