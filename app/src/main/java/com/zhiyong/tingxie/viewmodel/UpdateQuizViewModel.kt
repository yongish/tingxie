package com.zhiyong.tingxie.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkQuiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class UpdateQuizViewModel(application: Application) : AndroidViewModel(application) {
  val mRepository: QuizRepository = QuizRepository(application)

//  fun updateQuiz(quiz: Quiz?) = mRepository.updateQuiz(quiz)
  fun updateQuiz(quiz: NetworkQuiz): LiveData<Long> {
    val result = MutableLiveData<Long>()
    viewModelScope.launch(Dispatchers.IO) {
      val newId = mRepository.updateQuiz(quiz)
      result.postValue(newId)
    }
    return result
  }

}
