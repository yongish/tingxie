package com.zhiyong.tingxie.ui.word;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TermApi {
    @GET("term/{uid}")  //  todo: quizId
    Call<TermResponse> getTermsList(@Path("uid") String uid);
}
