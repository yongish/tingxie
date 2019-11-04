package com.zhiyong.tingxie.ui.word;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Term {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("createdAt")
    @Expose
    private long createdAt;

    @SerializedName("deletedAt")
    @Expose
    private long deletedAt;

    @SerializedName("quizId")
    @Expose
    private long quizId;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("word")
    @Expose
    private String word;

    @SerializedName("pinyin")
    @Expose
    private String pinyin;

    @SerializedName("timesCorrect")
    @Expose
    private int timesCorrect;
}
