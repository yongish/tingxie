package com.zhiyong.tingxie.network

import com.squareup.moshi.JsonClass
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual
import com.zhiyong.tingxie.ui.share.EnumQuizRole
import com.zhiyong.tingxie.ui.share.TingXieShareIndividual

@JsonClass(generateAdapter = true)
data class NetworkToken(val uid: String, val email: String, val token: String)

@JsonClass(generateAdapter = true)
data class NetworkQuizIdContainer(val quizIds: List<Long>)

@JsonClass(generateAdapter = true)
data class NetworkQuizRefreshContainer(
    val new_quizzes_remote: List<NetworkQuiz>,
    val deleted_quiz_ids: List<Long>,
    val missing_quiz_ids: List<Long>
    )

@JsonClass(generateAdapter = true)
data class NetworkWordItemIdentifierContainer(
    val quizId: Long, val pinyins: List<String>
    )

@JsonClass(generateAdapter = true)
data class NetworkRefreshWords (
  val email: String, val quizId: Long, val words: List<NetworkWordItem>
    )

@JsonClass(generateAdapter = true)
data class NetworkQuizContainer(val quizzes: List<NetworkQuiz>)

fun NetworkQuizContainer.asDatabaseModel(): List<Quiz> = quizzes.map {
  Quiz(it.quiz_id, it.date, it.title, it.total_words, it.not_learned, it.round)
}

@JsonClass(generateAdapter = true)
//data class NetworkQuiz(val email: String,
data class NetworkQuiz(val quiz_id: Long,
                       val date: Int,
                       val title: String,
                       val total_words: Int,
                       val not_learned: Int,
                       val round: Int)

@JsonClass(generateAdapter = true)
data class NetworkCreateQuiz(val client_quiz_id: Long,
                             val title: String,
                             val total_words: Int,
                             val not_learned: Int,
                             val round: Int,
                             val date: Int,
                             val email: String,
                             val name: String,
                             val role: String)

@JsonClass(generateAdapter = true)
data class NetworkQuizDeleted(val quiz_id: Long, val deleted: Boolean)

@JsonClass(generateAdapter = true)
data class NetworkPinyinContainer(val pinyins: List<String>)

@JsonClass(generateAdapter = true)
data class NetworkWordItem(val word_id: Long,
                           val characters: String,
                           val pinyin: String,
                           val asked: Boolean)

@JsonClass(generateAdapter = true)
data class NetworkIndividualContainer(val individuals: List<NetworkIndividual>)

fun NetworkIndividual.asDomainModel(): TingXieIndividual =
    TingXieIndividual(friend_email, name)

@JsonClass(generateAdapter = true)
data class NetworkIndividual(val friend_email: String, val name: String)

//fun NetworkFriendContainer.asDatabaseModel(): List<DatabaseFriend> {
//  return friends.map {
//    DatabaseFriend(it.email)
//  }
//}

@JsonClass(generateAdapter = true)
data class NetworkYourIndividualRequestContainer(
    val requests: List<NetworkYourIndividualRequest>)

@JsonClass(generateAdapter = true)
data class NetworkYourIndividualRequest(val email: String, val date: Int)

@JsonClass(generateAdapter = true)
data class NetworkOtherIndividualRequestContainer(
    val requests: List<NetworkOtherIndividualRequest>
    )

@JsonClass(generateAdapter = true)
data class NetworkOtherIndividualRequest(val email: String,
                                         val name: String,
                                         val date: Int)

@JsonClass(generateAdapter = true)
data class NetworkShareContainer(val shares: List<NetworkShare>)

@JsonClass(generateAdapter = true)
data class NetworkShare(val email: String,
                        val name: String,
                        val isShared: Boolean,
                        val role: EnumQuizRole)

fun NetworkShareContainer.asDomainModel(): List<TingXieShareIndividual> = shares.map {
  TingXieShareIndividual(it.email, it.name, it.isShared, it.role)
}
