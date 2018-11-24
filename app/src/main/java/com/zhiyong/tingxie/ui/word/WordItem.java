package com.zhiyong.tingxie.ui.word;

public class WordItem {
    private int wordId;
    private String word;
    private int pinyinId;
    private String pinyin;

    public WordItem(int wordId, String word, int pinyinId, String pinyin) {
        this.wordId = wordId;
        this.word = word;
        this.pinyinId = pinyinId;
        this.pinyin = pinyin;
    }

    public int getWordId() {
        return wordId;
    }

    public String getWord() {
        return word;
    }

    public int getPinyinId() {
        return pinyinId;
    }

    public String getPinyin() {
        return pinyin;
    }
}
