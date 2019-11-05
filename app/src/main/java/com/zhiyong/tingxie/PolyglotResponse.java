package com.zhiyong.tingxie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class PolyglotResponse {
    @SerializedName("status")
    @Expose
    @Getter @Setter
    private String status;
}
