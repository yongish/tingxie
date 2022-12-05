package com.zhiyong.tingxie.ui.question

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkWordItem
import com.zhiyong.tingxie.viewmodel.CrudStatus
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application, quizId: Long) :
  AndroidViewModel(application) {
  private val mRepository: QuizRepository = QuizRepository(application)

  private val _notCorrectWordsRandomOrder = MutableLiveData<List<NetworkWordItem>>()
  val notCorrectWordsRandomOrder: LiveData<List<NetworkWordItem>> =
    _notCorrectWordsRandomOrder

  private val _notCorrectWordsRandomOrderStatus = MutableLiveData<CrudStatus>()
  val notCorrectWordsRandomOrderStatus: LiveData<CrudStatus> =
    _notCorrectWordsRandomOrderStatus

  init {
    getNotCorrectWordsRandomOrder(quizId)
  }

  private fun getNotCorrectWordsRandomOrder(quizId: Long) {
    viewModelScope.launch {
      _notCorrectWordsRandomOrderStatus.value = CrudStatus.LOADING
      try {
        _notCorrectWordsRandomOrder.value =
          mRepository.notCorrectWordsRandomOrder(quizId)
        _notCorrectWordsRandomOrderStatus.value = CrudStatus.DONE
      } catch (e: Exception) {
        _notCorrectWordsRandomOrderStatus.value = CrudStatus.ERROR
      }
    }
  }
}
