package com.zhiyong.tingxie.ui.word;

import java.util.List;

public class CedictDefinition {
    String traditional;
    String simplified;
    String pinyin;
    List<String> definitions;

    public String getTraditional() {
        return traditional;
    }

    public void setTraditional(String traditional) {
        this.traditional = traditional;
    }

    public String getSimplified() {
        return simplified;
    }

    public void setSimplified(String simplified) {
        this.simplified = simplified;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }
}
