package com.zhiyong.tingxie.network

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual
import com.zhiyong.tingxie.ui.main.QuizItem
import com.zhiyong.tingxie.ui.share.EnumQuizRole
import com.zhiyong.tingxie.ui.share.TingXieShareIndividual
import com.zhiyong.tingxie.ui.word.WordItem
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class NetworkToken(
  val uid: String,
  val email: String,
  val token: String
)

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
data class NetworkRefreshWords(
  val email: String, val quizId: Long, val words: List<NetworkWordItem>
)

@JsonClass(generateAdapter = true)
data class NetworkQuizContainer(val quizzes: List<NetworkQuiz>)

fun NetworkQuizContainer.asDatabaseModel(): List<Quiz> = quizzes.map {
  Quiz(it.quizId, it.date, it.title, it.numWords, it.numNotCorrect, it.round)
}

@JsonClass(generateAdapter = true)
data class NetworkQuiz(
  val quizId: Long,
  val title: String,
  val date: Int,
  val email: String,
  val role: String,
  val numWords: Int,
  val numNotCorrect: Int,
  val round: Int
)

fun NetworkQuiz.asDomainModel(): QuizItem =
  QuizItem(quizId, date, title, numWords, numNotCorrect, round)

@JsonClass(generateAdapter = true)
data class NetworkCreateQuiz(
  val title: String,
  val date: Int,
  val name: String,
  val email: String,
//  val total_words: Int,
//  val not_learned: Int,
//  val round: Int,
//  val role: String
)

@JsonClass(generateAdapter = true)
data class NetworkQuizDeleted(val quiz_id: Long, val deleted: Boolean)

@JsonClass(generateAdapter = true)
data class NetworkPinyinContainer(val pinyins: List<String>)

@JsonClass(generateAdapter = true)
data class NetworkWordItem(
  val id: Long,
  val quizId: Long,
  val pinyin: String,
  val characters: String
)

fun NetworkWordItem.asDomainModel(quizId: Long): WordItem =
  WordItem(id, quizId, characters, pinyin)

@JsonClass(generateAdapter = true)
data class NetworkCreateWord(
  val characters: String,
  val pinyin: String
)

@JsonClass(generateAdapter = true)
data class NetworkQuestion(
  val pinyin: String,
  val characters: String
)

@JsonClass(generateAdapter = true)
data class NetworkCorrectRecord(
  val wordId: Long,
  val email: String,
)

@JsonClass(generateAdapter = true)
data class NetworkCreateMember(
  val userName: String,
  val email: String,
  val role: String
)

@JsonClass(generateAdapter = true)
data class NetworkCreateGroup(
  val name: String,
  val creatorName: String,
  val creatorEmail: String,
  val members: List<NetworkCreateMember>
)

@Parcelize
@JsonClass(generateAdapter = true)
data class NetworkGroup(
  val id: Long,
  val name: String,
  val role: String,
  val numMembers: Int
) : Parcelable

@JsonClass(generateAdapter = true)
data class NetworkGroupMember(val userName: String, val email: String, val role: String)

// todo: Not using these data classes below. Should clean up.
@JsonClass(generateAdapter = true)
data class NetworkIndividualContainer(val individuals: List<NetworkIndividual>)

fun NetworkIndividual.asDomainModel(): TingXieIndividual =
  TingXieIndividual(to_email, to_name, status)

@JsonClass(generateAdapter = true)
data class NetworkIndividual(
  val from_email: String,
  val to_email: String,
  val from_name: String,
  val to_name: String,
  val status: String
)

@JsonClass(generateAdapter = true)
data class NetworkYourIndividualRequestContainer(
  val requests: List<NetworkYourIndividualRequest>
)

@JsonClass(generateAdapter = true)
data class NetworkYourIndividualRequest(val email: String, val requestEmail: String)

@JsonClass(generateAdapter = true)
data class NetworkOtherIndividualRequestContainer(
  val requests: List<NetworkOtherIndividualRequest>
)

@JsonClass(generateAdapter = true)
data class NetworkOtherIndividualRequest(
  val email: String,
  val name: String,
  val date: Int
)

@JsonClass(generateAdapter = true)
data class NetworkShareContainer(val shares: List<NetworkShare>)

@JsonClass(generateAdapter = true)
data class NetworkShare(
  val email: String,
  val name: String,
  val isShared: Boolean,
  val role: EnumQuizRole
)

// may delete this.
fun NetworkShareContainer.asDomainModel(): List<TingXieShareIndividual> = shares.map {
  TingXieShareIndividual(it.email, it.name, it.isShared, it.role)
}
