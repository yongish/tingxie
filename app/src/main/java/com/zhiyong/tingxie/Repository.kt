package com.zhiyong.tingxie

import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.network.NetworkQuiz
import com.zhiyong.tingxie.ui.word.WordItem

// Contains the QuizRespository functions we want to fake in FakeQuizRepository, to
// test QuizRepository
interface Repository {
  suspend fun getQuizzes(): List<NetworkQuiz>
  suspend fun createQuiz(title: String, date: Int): Long
  suspend fun updateQuiz(quiz: NetworkQuiz): Int
  suspend fun deleteQuiz(quizId: Long): String
  suspend fun putToken(uid: String, email: String, token: String)
  suspend fun addWord(quizId: Long, wordString: String?, pinyinString: String?): Long
  fun insertQuizPinyin(quizPinyin: QuizPinyin?)
  suspend fun getWordItemsOfQuiz(quizId: Long): List<WordItem>?
  suspend fun deleteWord(id: Long): String
}
