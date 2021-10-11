package com.zhiyong.tingxie.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.db.Quiz

open class UpdateQuizViewModel(application: Application) : AndroidViewModel(application) {
  val mRepository: QuizRepository = QuizRepository(-1, application)

  fun updateQuiz(quiz: Quiz?) = mRepository.updateQuiz(quiz)
}
