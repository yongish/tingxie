package com.zhiyong.tingxie.ui.share

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

class ShareIndividualViewModel(quizId: Long, application: Application) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _shares = MutableLiveData<List<TingXieShareIndividual>>()
  val shares: LiveData<List<TingXieShareIndividual>>
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
        when(e) {
          is NoSuchElementException -> {
            _status.value = Status.DONE
          }
          else -> {
            Toast.makeText(getApplication(), e.message, Toast.LENGTH_LONG).show()
            _status.value = Status.ERROR
          }
        }
      }
    }
  }

  fun addShare(quizId: Long, shareIndividual: TingXieShareIndividual) {
    viewModelScope.launch { repository.addShare(quizId, shareIndividual) }
  }

  fun setAllShared(quizId: Long, shared: Boolean) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _shares.value = repository.shareAll(quizId, shared)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _shares.value = ArrayList()
        _status.value = Status.ERROR
      }
    }
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
