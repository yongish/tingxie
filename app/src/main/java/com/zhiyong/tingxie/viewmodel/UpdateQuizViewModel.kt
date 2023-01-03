package com.zhiyong.tingxie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.Repository
import com.zhiyong.tingxie.network.NetworkQuiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class UpdateQuizViewModel(private val mRepository: Repository) : ViewModel() {

  fun updateQuiz(quiz: NetworkQuiz): LiveData<Int> {
    val result = MutableLiveData<Int>()
    viewModelScope.launch(Dispatchers.IO) {
      val newId = mRepository.updateQuiz(quiz)
      result.postValue(newId)
    }
    return result
  }

}
