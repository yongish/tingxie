package com.zhiyong.tingxie.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface Service {
  @GET("quizzes")
  suspend fun getQuizzes(@Query("email") email: String): NetworkQuizContainer

  @PUT("refreshQuizzes")
  suspend fun getNewQuizzes(
      @Query("email") email: String,
      @Body quizIds: NetworkQuizIdContainer
  ): NetworkQuizRefreshContainer

  @PUT("refreshWordItemsOfQuiz")
  suspend fun getWordItemsOfQuiz(
      @Query("email") email: String,
      @Query("quizId") quizId: Long,
      @Body pinyinContainer: NetworkPinyinContainer
  ): NetworkWordItemRefreshContainer

  @PUT("quizzes")
  suspend fun putQuizzes(@Query("email") email: String,
                         @Body quizzes: NetworkQuizContainer): Boolean

  @PUT("wordItems")
  suspend fun putWordItems(@Query("email") email: String,
                           @Body wordItems: NetworkWordItemContainer): Boolean

  @GET("shares")
  suspend fun getShares(@Query("email") email: String,
                        @Query("quizId") quizId: Long): NetworkShareContainer

  @POST("shares/userEmail/{email}/quizId/{quizId}")
  suspend fun postShare(@Path("email") email: String,
                        @Path("quizId") quizId: Long,
                        @Body share: NetworkShare): NetworkShare

  @PUT("shares/userEmail/{userEmail}/quizzes/{quizId}/shared/{shared}")
  suspend fun putShareAll(@Path("email") email: String,
                          @Path("quizId") quizId: Long,
                          @Path("shared") shared: Boolean): NetworkShare

  @DELETE("shares/userEmail/{userEmail}/quizzes/{quizId}/email/{email}")
  suspend fun deleteShare(@Path("userEmail") userEmail: String,
                          @Path("quizId") quizId: Long,
                          @Path("email") email: String): Boolean

  @GET("friends")
  suspend fun getFriends(@Query("email") email: String): NetworkIndividualContainer

  @POST("friends")
  suspend fun postFriend(@Query("email") email: String,
                         @Body individual: NetworkIndividual): NetworkIndividual

  @DELETE("friends/userEmail/{userEmail}/friendEmail/{email}")
  suspend fun deleteFriend(@Path("userEmail") userEmail: String,
                           @Path("email") email: String): Boolean

  @GET("yourIndividualRequests")
  suspend fun getYourIndividualRequests(@Query("email") email: String)
      : NetworkYourIndividualRequestContainer

  @POST("yourIndividualRequest")
  suspend fun postYourIndividualRequest(
      @Query("email") email: String, @Body request: NetworkYourIndividualRequest
  ): NetworkYourIndividualRequest

  @DELETE("yourIndividualRequest/userEmail/{userEmail}/otherEmail/{otherEmail}")
  suspend fun deleteYourIndividualRequest(
      @Path("userEmail") userEmail: String,
      @Path("otherEmail") otherEmail: String
  ): Boolean

  @GET("otherIndividualRequests")
  suspend fun getOtherIndividualRequests(@Query("email") email: String)
      : NetworkOtherIndividualRequestContainer

  @PUT("otherIndividualRequest/requesterEmail/{userEmail}/responderEmail/{otherEmail}/accept/{accept}")
  suspend fun putOtherIndividualRequest(
      @Path("requesterEmail") requesterEmail: String,
      @Path("responderEmail") responderEmail: String,
      @Path("accept") accept: Boolean
  ): Boolean
}

object TingXieNetwork {
  private val retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:3000/") // todo: Replace with production URL.
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

  val tingxie: Service = retrofit.create(Service::class.java)
}
