package com.zhiyong.tingxie.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface Service {
  @GET("quizzes")
  suspend fun getQuizzes(): NetworkQuizContainer
}

object TingXieNetwork {
  private val retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:3000/") // todo: Replace with production URL.
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

  val tingxie = retrofit.create(Service::class.java)
}
