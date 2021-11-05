package com.zhiyong.tingxie.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface Service {
  @GET("tingxie")
  suspend fun getQuizzes(): NetworkQuizContainer
}

object TingXieNetwork {
  private val retrofit = Retrofit.Builder()
    .baseUrl("https://android-kotlin-fun-mars-server.appspot.com/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

  val tingxie = retrofit.create(Service::class.java)
}
