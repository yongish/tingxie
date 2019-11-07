package com.zhiyong.tingxie.ui.word;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zhiyong.tingxie.PolyglotResponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class TermResponse extends PolyglotResponse {
    @SerializedName("terms")
    @Expose
    @Getter @Setter
    private List<Term> terms;
}
