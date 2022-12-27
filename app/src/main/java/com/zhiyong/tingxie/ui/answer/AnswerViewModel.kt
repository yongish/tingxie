package com.zhiyong.tingxie.ui.answer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkQuiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnswerViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = QuizRepository(application)

  fun upsertCorrectRecord(wordId: Long): LiveData<Int> {
    val result = MutableLiveData<Int>()
    viewModelScope.launch(Dispatchers.IO) {
      val numRecords = repository.upsertCorrectRecord(wordId)
      result.postValue(numRecords)
    }
    return result
  }

  fun updateQuiz(quizItem: NetworkQuiz): LiveData<Int> {
    val result = MutableLiveData<Int>()
    viewModelScope.launch(Dispatchers.IO) {
      val numRecords = repository.updateQuiz(quizItem)
      result.postValue(numRecords)
    }
    return result
  }
}
