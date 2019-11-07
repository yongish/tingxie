package com.zhiyong.tingxie.ui.word;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class Term {
    @SerializedName("id")
    @Expose
    @Getter @Setter
    private long id;

    @SerializedName("createdAt")
    @Expose
    @Getter @Setter
    private long createdAt;

    @SerializedName("deletedAt")
    @Expose
    @Getter @Setter
    private long deletedAt;

    @SerializedName("quizId")
    @Expose
    @Getter @Setter
    private long quizId;

    @SerializedName("uid")
    @Expose
    @Getter @Setter
    private String uid;

    @SerializedName("word")
    @Expose
    @Getter @Setter
    private String word;

    @SerializedName("pinyin")
    @Expose
    @Getter @Setter
    private String pinyin;

    @SerializedName("timesCorrect")
    @Expose
    @Getter @Setter
    private int timesCorrect;
}
