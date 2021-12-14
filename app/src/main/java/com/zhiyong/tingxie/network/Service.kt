package com.zhiyong.tingxie.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface Service {
  @GET("quizzes")
  suspend fun getQuizzes(@Query("email") email: String): NetworkQuizContainer

  @GET("shares")
  suspend fun getShares(@Query("email") email: String,
                        @Query("quizId") quizId: Long): NetworkShareContainer

  @POST("shares/userEmail/{email}/quizId/{quizId}")
  suspend fun postShare(@Path("email") email: String,
                        @Path("quizId") quizId: Long,
                        @Body share: NetworkShare): NetworkShare

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

  @GET("groups")
  suspend fun getGroups(@Query("email") email: String): NetworkGroupContainer

  @POST("groups")
  suspend fun postGroup(@Query("email") email: String,
                        @Body group: NetworkGroup): NetworkGroup

  @DELETE("groups/{name}/userEmail/{userEmail}/friendEmail/{email}")
  suspend fun deleteGroup(@Path("name") name: String,
                          @Path("userEmail") userEmail: String,
                          @Path("email") email: String): Boolean

  @GET("shareGroups")
  suspend fun getShareGroups(
      @Query("email") email: String, @Query("quizId") quizId: Long
  ): NetworkShareGroupContainer

  @POST("shareGroup/{name}/email/{email}/quizId/{quizId}")
  suspend fun postShareGroup(@Path("name") name: String,
                             @Path("email") email: String,
                             @Path("quizId") quizId: Long)

  @DELETE("shareGroup/{name}/email/{email}/quizId/{quizId}")
  suspend fun deleteShareGroup(@Path("name") name: String,
                               @Path("email") email: String,
                               @Path("quizId") quizId: Long)
}

object TingXieNetwork {
  private val retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:3000/") // todo: Replace with production URL.
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

  val tingxie: Service = retrofit.create(Service::class.java)
}
