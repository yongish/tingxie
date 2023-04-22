package com.zhiyong.tingxie.network

import com.zhiyong.tingxie.ui.main.MigrateLocal
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*


interface Service {
  @PUT("tokens")
  suspend fun putToken(@Body token: NetworkToken): String

  @GET("users/{email}/quizzes")
  suspend fun getQuizzes(@Path("email") email: String): List<NetworkQuiz>

  @POST("migrateLocal")
  suspend fun migrateLocal(@Body localData: MigrateLocal): List<NetworkQuiz>

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

  @GET("users/{email}/groups")
  suspend fun getGroups(@Path("email") email: String): MutableList<NetworkGroup>

  @POST("groups")
  suspend fun createGroup(@Body group: NetworkCreateGroup): String

  @DELETE("groups/{groupId}")
  suspend fun deleteGroup(
    @Path("groupId") groupId: Long,
    @Query("requesterName") requesterName: String,
    @Query("requesterEmail") requesterEmail: String
  ): Int

  @GET("groups/{groupId}/members")
  suspend fun getGroupMembers(@Path("groupId") groupId: Long): MutableList<NetworkGroupMember>

  @POST("groups/{groupId}/email/{email}/role/{role}")
  suspend fun addGroupMemberOrReturnNoUser(
    @Path("groupId") groupId: Long,
    @Path("email") email: String,
    @Path("role") role: String
  ): NetworkGroupMember

  @PUT("groups/{groupId}/email/{email}/role/{role}")
  suspend fun changeGroupRole(
    @Path("groupId") groupId: Long,
    @Path("email") email: String,
    @Path("role") role: String
  ): Int

  @DELETE("groups/{groupId}/members/{email}")
  suspend fun deleteGroupMember(
    @Path("groupId") groupId: Long,
    @Path("email") email: String,
    @Query("requesterName") requesterName: String,
    @Query("requesterEmail") requesterEmail: String
  ): Int

  @GET("quizzes/{quizId}/users")
  suspend fun getUsersOfQuiz(@Path("quizId") quizId: Long): List<NetworkGroupMember>

  @POST("quizzes/{quizId}/users")
  suspend fun addQuizMemberOrReturnNoUser(
    @Path("quizId") quizId: Long,
    @Body addQuizUser: NetworkAddQuizUser
  ): NetworkGroupMember

  @PUT("quizzes/{quizId}/users")
  suspend fun changeQuizRole(
    @Path("quizId") quizId: Long,
    @Body addQuizUser: NetworkAddQuizUser
  ): Int

  @DELETE("quizzes/{quizId}/users/{email}")
  suspend fun removeQuizMember(
    @Path("quizId") quizId: Long,
    @Path("email") email: String,
    @Query("requesterName") requesterName: String,
    @Query("requesterEmail") requesterEmail: String
  ): Int

  @GET("quizzes/{quizId}/groups")
  suspend fun getGroupsOfQuiz(@Path("quizId") quizId: Long): List<NetworkGroup>

  @PUT("quizzes/{quizId}/groups/{groupId}")
  suspend fun addGroupMembersToQuiz(
    @Path("quizId") quizId: Long,
    @Path("groupId") groupId: Long,
  ): String

  // todo: Not developing these endpoints below. Should clean up.
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
  private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
    return try {
      // Create a trust manager that does not validate certificate chains
      val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
        object : X509TrustManager {
          @Throws(CertificateException::class)
          override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String?) {
          }

          @Throws(CertificateException::class)
          override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String?) {
          }

          override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
          }
        }
      )

      // Install the all-trusting trust manager
      val sslContext: SSLContext = SSLContext.getInstance("SSL")
      sslContext.init(null, trustAllCerts, SecureRandom())

      // Create an ssl socket factory with our all-trusting manager
      val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
      val builder = OkHttpClient.Builder()
      builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
      builder.hostnameVerifier { _, _ -> true }
      builder
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }
  private val retrofit = Retrofit.Builder()
//    .baseUrl("http://10.0.2.2:9000/") // Emulator
    .baseUrl("https://h1b1club.com/") // Production URL.
    .addConverterFactory(MoshiConverterFactory.create().asLenient())
    .client(getUnsafeOkHttpClient().build())
    .build()

  val tingxie: Service = retrofit.create(Service::class.java)
}
