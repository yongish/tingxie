package com.zhiyong.tingxie

import com.zhiyong.tingxie.network.NetworkQuiz

// Contains the QuizRespository functions we want to fake in FakeQuizRepository, to
// test QuizRepository
interface QuizRepositoryInterface {
  suspend fun getQuizzes(): List<NetworkQuiz>
  suspend fun createQuiz(title: String, date: Int): Long
  suspend fun updateQuiz(quiz: NetworkQuiz): Int
  suspend fun deleteQuiz(quizId: Long): String
  suspend fun putToken(uid: String, email: String, token: String)
}
