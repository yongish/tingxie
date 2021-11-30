package com.zhiyong.tingxie.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface Service {
  @GET("quizzes")
  suspend fun getQuizzes(): NetworkQuizContainer

  @GET("friends/{email}")
  suspend fun getFriends(@Path("email") email: String): NetworkFriendContainer

  @POST("friends")
  suspend fun postFriend(@Body friend: NetworkFriend): NetworkFriend

  @DELETE("friends/{email}")
  suspend fun deleteFriend(@Path("email") email: String): Boolean
}

object TingXieNetwork {
  private val retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:3000/") // todo: Replace with production URL.
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

  val tingxie = retrofit.create(Service::class.java)
}
