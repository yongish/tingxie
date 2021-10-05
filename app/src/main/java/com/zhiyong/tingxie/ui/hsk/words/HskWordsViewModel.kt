package com.zhiyong.tingxie.ui.hsk.words

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.getDatabase

class HskWordsViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(getDatabase(application), -1)

//  fun getHskQuestions(level: Int) = repository.getHskQuestions(level)
  fun getHskQuestions(level: Int): LiveData<List<HskQuestionItem>> = repository.getHskQuestions(level)
}