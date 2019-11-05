package com.zhiyong.tingxie.ui.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zhiyong.tingxie.PolyglotResponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class QuizResponse extends PolyglotResponse {
    @SerializedName("quizzes")
    @Expose
    @Getter @Setter
    private List<Quiz> quizzes;
}
