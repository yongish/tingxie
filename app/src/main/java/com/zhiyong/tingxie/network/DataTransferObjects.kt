package com.zhiyong.tingxie.network

import com.squareup.moshi.JsonClass
import com.zhiyong.tingxie.db.Quiz

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
