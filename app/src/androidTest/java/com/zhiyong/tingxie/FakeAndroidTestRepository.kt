package com.zhiyong.tingxie

import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.network.NetworkQuiz
import com.zhiyong.tingxie.ui.word.WordItem

class FakeAndroidTestRepository : Repository {
  override suspend fun getQuizzes(): List<NetworkQuiz> {
    TODO("Not yet implemented")
  }

  override suspend fun createQuiz(title: String, date: Int): Long {
    TODO("Not yet implemented")
  }

  override suspend fun updateQuiz(quiz: NetworkQuiz): Int {
    TODO("Not yet implemented")
  }

  override suspend fun deleteQuiz(quizId: Long): String {
    TODO("Not yet implemented")
  }

  override suspend fun putToken(uid: String, email: String, token: String) {
    TODO("Not yet implemented")
  }

  override suspend fun addWord(
    quizId: Long,
    wordString: String?,
    pinyinString: String?
  ): Long {
    TODO("Not yet implemented")
  }

  override fun insertQuizPinyin(quizPinyin: QuizPinyin?) {
    TODO("Not yet implemented")
  }

  override suspend fun getWordItemsOfQuiz(quizId: Long): List<WordItem>? {
    TODO("Not yet implemented")
  }

  override suspend fun deleteWord(id: Long): String {
    TODO("Not yet implemented")
  }
}
