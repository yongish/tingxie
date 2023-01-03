package com.zhiyong.tingxie

import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.network.NetworkQuiz
import com.zhiyong.tingxie.ui.share.EnumQuizRole
import com.zhiyong.tingxie.ui.word.WordItem

class FakeQuizRepository(private var quizzes: MutableList<NetworkQuiz> = mutableListOf()) :
  Repository {
  override suspend fun getQuizzes(): List<NetworkQuiz> = quizzes

  override suspend fun createQuiz(title: String, date: Int): Long {
    val newId = quizzes.size.toLong()
    quizzes += NetworkQuiz(
      newId,
      title,
      date,
      "test@email.com",
      EnumQuizRole.OWNER.name,
      0,
      0,
      1
    )
    return newId
  }

  override suspend fun updateQuiz(quiz: NetworkQuiz): Int {
    quizzes[quizzes.indexOf(quiz)] = quiz
    return 1
  }

  override suspend fun deleteQuiz(quizId: Long): String {
    quizzes.removeAt(quizId.toInt())
    return "1"
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
