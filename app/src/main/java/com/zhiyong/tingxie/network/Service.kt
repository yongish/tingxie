package com.zhiyong.tingxie.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface Service {
  @GET("quizzes")
  suspend fun getQuizzes(): NetworkQuizContainer

  @GET("quizzes/{quizId}/shares")
  suspend fun getShares(@Path("quizId") quizId: Long): NetworkShareContainer

  @POST("quizzes/{quizId}/shares")
  suspend fun postShare(@Body share: NetworkShare): NetworkShare

  @DELETE("quizzes/{quizId}/shares/{email}")
  suspend fun deleteShare(
      @Path("quizId") quizId: Long, @Path("email") email: String
  ): Boolean

  @GET("friends/{email}")
  suspend fun getFriends(@Path("email") email: String): NetworkIndividualContainer

  @POST("friends")
  suspend fun postFriend(@Body individual: NetworkIndividual): NetworkIndividual

  @DELETE("friends/{email}")
  suspend fun deleteFriend(@Path("email") email: String): Boolean

  @GET("friends/{email}/groups")
  suspend fun getGroups(@Path("email") email: String): NetworkGroupContainer

  @GET("friends/{email}/quizzes/{quizId}/groups")
  suspend fun getGroups(@Path("email") email: String,
                        @Query("quizId") quizId: Long): NetworkGroupContainer

  @POST("friends/{email}/groups")
  suspend fun postGroup(@Body group: NetworkGroup): NetworkGroup

  @POST("friends/{email}")

  @PUT("friends/{email}/groups")
  suspend fun putGroup(@Body group: NetworkGroup): NetworkGroup

  @DELETE("friends/{email}/groups/{name}")
  suspend fun deleteGroup(@Path("email") email: String,
                          @Path("name") name: String): Boolean
}

object TingXieNetwork {
  private val retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:3000/") // todo: Replace with production URL.
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

  val tingxie = retrofit.create(Service::class.java)
}
