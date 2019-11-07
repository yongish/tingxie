package com.zhiyong.tingxie.ui.main;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QuizApi {
    @GET("quiz/{uid}")
    Call<QuizResponse> getQuizList(@Path("uid") String uid);
}
