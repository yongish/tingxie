package com.zhiyong.tingxie.ui.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class Quiz {
    @SerializedName("id")
    @Expose
    @Getter
    @Setter
    private long id;
    @SerializedName("uid")
    @Expose
    @Getter @Setter
    private String uid;
    @SerializedName("deleted")
    @Expose
    @Getter @Setter
    private boolean deleted;
    @SerializedName("date")
    @Expose
    @Getter @Setter
    private int date;
    @SerializedName("title")
    @Expose
    @Getter @Setter
    private String title;
    @SerializedName("totalTerms")
    @Expose
    @Getter @Setter
    private int totalTerms;
    @SerializedName("notLearned")
    @Expose
    @Getter @Setter
    private int notLearned;
    @SerializedName("roundsCompleted")
    @Expose
    @Getter @Setter
    private int roundsCompleted;
}
