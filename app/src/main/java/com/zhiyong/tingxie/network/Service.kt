package com.zhiyong.tingxie.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface Service {
  @PUT("token")
  suspend fun putToken(@Body token: NetworkToken): String

  @PUT("quizzes/{email}")
  suspend fun refreshQuizzes(
    @Path("email") email: String,
    @Body quizIds: List<NetworkQuizDeleted>
  ): NetworkQuizRefreshContainer

  @POST("quiz")
  suspend fun postQuiz(@Body quiz: NetworkCreateQuiz): Long

  @DELETE("quiz/email/{email}/quiz_id/{quizId}")
  suspend fun deleteQuiz(
    @Path("email") email: String,
    @Path("quizId") quizId: Long
  ): String

  @PUT("words")
  suspend fun refreshWords(
    @Body refreshWordsContainer: NetworkRefreshWords
  ): List<NetworkWordItem>

  @POST("word")
  suspend fun postWord(@Body word: NetworkCreateWord): Long

  @GET("shares/email/{email}/quiz_id/{quizId}")
  suspend fun getShares(
    @Path("email") email: String,
    @Path("quizId") quizId: Long
  ): List<NetworkShare>

  @POST("shares/userEmail/{email}/quizId/{quizId}")
  suspend fun postShare(
    @Path("email") email: String,
    @Path("quizId") quizId: Long,
    @Body share: NetworkShare
  ): NetworkShare

  @PUT("shares/userEmail/{userEmail}/quizzes/{quizId}/shared/{shared}")
  suspend fun putShareAll(
    @Path("email") email: String,
    @Path("quizId") quizId: Long,
    @Path("shared") shared: Boolean
  ): NetworkShare

  @DELETE("shares/userEmail/{userEmail}/quizzes/{quizId}/email/{email}")
  suspend fun deleteShare(
    @Path("userEmail") userEmail: String,
    @Path("quizId") quizId: Long,
    @Path("email") email: String
  ): Boolean

  @GET("userExists/{email}")
  suspend fun checkUserExists(@Path("email") email: String): String

  @GET("friends/{email}/party/{party}/status/{status}")
  suspend fun getFriends(
    @Path("email") email: String,
    @Path("party") party: String,
    @Path("status") status: String
  ): List<NetworkIndividual>

  @POST("friends")
  suspend fun postFriend(@Body individual: NetworkIndividual): NetworkIndividual

  @DELETE("friends/userEmail/{userEmail}/friendEmail/{email}")
  suspend fun deleteFriend(
    @Path("userEmail") userEmail: String,
    @Path("email") email: String
  ): Boolean

  @DELETE("yourIndividualRequest/userEmail/{userEmail}/otherEmail/{otherEmail}")
  suspend fun deleteYourIndividualRequest(
    @Path("userEmail") userEmail: String,
    @Path("otherEmail") otherEmail: String
  ): Boolean

  @PUT("otherIndividualRequest/requesterEmail/{userEmail}/responderEmail/{otherEmail}/accept/{accept}")
  suspend fun putOtherIndividualRequest(
    @Path("requesterEmail") requesterEmail: String,
    @Path("responderEmail") responderEmail: String,
    @Path("accept") accept: Boolean
  ): Boolean
}

object TingXieNetwork {
  private val retrofit = Retrofit.Builder()
//    .baseUrl("http://10.0.2.2:8000/") // todo: Replace with production URL.
    .baseUrl("http://10.0.2.2:9000/") // todo: Replace with production URL.
    .addConverterFactory(MoshiConverterFactory.create().asLenient())
    .build()

  val tingxie: Service = retrofit.create(Service::class.java)
}
