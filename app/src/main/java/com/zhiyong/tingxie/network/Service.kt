package com.zhiyong.tingxie.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface Service {
  @PUT("tokens")
  suspend fun putToken(@Body token: NetworkToken): String

  @GET("users/{email}/quizzes")
  suspend fun getQuizzes(@Path("email") email: String): List<NetworkQuiz>

  @POST("quizzes")
  suspend fun postQuiz(@Body quiz: NetworkCreateQuiz): Long

  @PUT("quizzes")
  suspend fun putQuiz(@Body quiz: NetworkQuiz): Int

  @DELETE("quizzes/{quizId}")
  suspend fun deleteQuiz(
    @Path("quizId") quizId: Long,
    @Query("requesterName") requesterName: String,
    @Query("requesterEmail") requesterEmail: String,
    @Query("email") email: String
  ): String

  @GET("/quizzes/{quizId}/words")
  suspend fun getWordItemsOfQuiz(
    @Path("quizId") quizId: Long
  ): List<NetworkWordItem>

  @POST("/quizzes/{quizId}/words")
  suspend fun postWord(@Path("quizId") quizId: Long, @Body word: NetworkCreateWord): Long

  @DELETE("words/{id}")
  suspend fun deleteWord(@Path("id") id: Long): String

  @GET("notCorrectWordsRandomOrder")
  suspend fun notCorrectWordsRandomOrder(
    @Query("quizId") quizId: Long,
    @Query("email") email: String
  ): List<NetworkWordItem>

  @PUT("questions")
  suspend fun upsertCorrectRecord(@Body asked: NetworkCorrectRecord): Int

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

  @PUT("friends")
  suspend fun putFriend(@Body friend: NetworkIndividual): NetworkIndividual

  @DELETE("friends/{from_email}/to_email/{to_email}")
  suspend fun deleteFriend(
    @Path("from_email") from_email: String,
    @Path("to_email") to_email: String
  ): String
//  @PUT("otherIndividualRequest/requesterEmail/{userEmail}/responderEmail/{otherEmail}/accept/{accept}")
//  suspend fun putOtherIndividualRequest(
//    @Path("requesterEmail") requesterEmail: String,
//    @Path("responderEmail") responderEmail: String,
//    @Path("accept") accept: Boolean
//  ): Boolean

//  @PUT("quizzes/{email}")
//  suspend fun refreshQuizzes(
//    @Path("email") email: String,
//    @Body quizIds: List<NetworkQuizDeleted>
//  ): NetworkQuizRefreshContainer
//@PUT("words")
//suspend fun refreshWords(
//  @Body refreshWordsContainer: NetworkRefreshWords
//): List<NetworkWordItem>

}

object TingXieNetwork {
  private val retrofit = Retrofit.Builder()
//    .baseUrl("http://10.0.2.2:8000/") // todo: Replace with production URL.
    .baseUrl("http://10.0.2.2:9000/") // todo: Replace with production URL.
    .addConverterFactory(MoshiConverterFactory.create().asLenient())
    .build()

  val tingxie: Service = retrofit.create(Service::class.java)
}
