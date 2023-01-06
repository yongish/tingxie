package com.zhiyong.tingxie

import android.app.Application
import androidx.annotation.VisibleForTesting

object ServiceLocator {
  private val lock = Any()

  @Volatile
  var quizRepository: Repository? = null
    @VisibleForTesting set

  fun provideQuizRepository(application: Application): Repository {
    synchronized(this) {
      return quizRepository ?: createQuizRepository(application)
    }
  }

  private fun createQuizRepository(application: Application): Repository {
    val newRepo = QuizRepository(application)
    quizRepository = newRepo
    return newRepo
  }

  @VisibleForTesting
  fun resetRepository() {

    // https://developer.android.com/codelabs/advanced-android-kotlin-training-testing-test-doubles#8
    // says to delete all objects, but I didn't implement that here.
    quizRepository = null
  }
}
