package com.zhiyong.tingxie.network

import com.squareup.moshi.JsonClass
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.ui.friend.TingXieIndividual
import com.zhiyong.tingxie.ui.share.EnumQuizRole
import com.zhiyong.tingxie.ui.share.TingXieShare

@JsonClass(generateAdapter = true)
data class NetworkQuizContainer(val quizzes: List<NetworkQuiz>)

@JsonClass(generateAdapter = true)
data class NetworkQuiz(val id: Long,
                       val date: Int,
                       val title: String,
                       val total_words: Int,
                       val not_learned: Int,
                       val round: Int)

fun NetworkQuizContainer.asDatabaseModel(): List<Quiz> {
  return quizzes.map {
    Quiz(it.id, it.date, it.title, it.total_words, it.not_learned, it.round)
  }
}

@JsonClass(generateAdapter = true)
data class NetworkIndividualContainer(val individuals: List<NetworkIndividual>)

@JsonClass(generateAdapter = true)
data class NetworkIndividual(val email: String, val firstName: String, val lastName: String)

fun NetworkIndividualContainer.asDomainModel(): List<TingXieIndividual> {
  return individuals.map { TingXieIndividual(it.email, it.firstName, it.lastName) }
}

//fun NetworkFriendContainer.asDatabaseModel(): List<DatabaseFriend> {
//  return friends.map {
//    DatabaseFriend(it.email)
//  }
//}

@JsonClass(generateAdapter = true)
data class NetworkShareContainer(val shares: List<NetworkShare>)

@JsonClass(generateAdapter = true)
data class NetworkShare(val email: String,
                        val firstName: String,
                        val lastName: String,
                        val isShared: Boolean,
                        val role: EnumQuizRole)

fun NetworkShareContainer.asDomainModel(): List<TingXieShare> {
  return shares.map {
    TingXieShare(it.email, it.firstName, it.lastName, it.isShared, it.role)
  }
}
