package com.zhiyong.tingxie.ui.word;

import java.util.List;

public class CedictDefinition {
    String traditional;
    String simplified;
    String pinyin;
    List<String> defintions;

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

    public List<String> getDefintions() {
        return defintions;
    }

    public void setDefintions(List<String> defintions) {
        this.defintions = defintions;
    }
}
