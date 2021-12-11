package com.zhiyong.tingxie.ui.share

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.ui.friend.Status
import kotlinx.coroutines.launch

class ShareIndividualViewModel(quizId: Long, application: Application) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _shares = MutableLiveData<List<TingXieShare>>()
  val shares: LiveData<List<TingXieShare>>
    get() = _shares

  init {
    getShares(quizId)
  }

  private fun getShares(quizId: Long) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _shares.value = repository.getShares(quizId)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _shares.value = ArrayList()
        _status.value = Status.ERROR
      }
    }
  }

  fun addShare(quizId: Long, share: TingXieShare) {
    viewModelScope.launch { repository.addShare(quizId, share) }
  }

  fun deleteShare(quizId: Long, email: String) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        repository.deleteShare(quizId, email)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _status.value = Status.ERROR
      }
    }
  }
}
