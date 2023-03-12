package com.zhiyong.tingxie.ui.share

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkGroup
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

class ShareGroupViewModel(application: Application, quizId: Long) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _groups = MutableLiveData<List<NetworkGroup>>()
  val groups: LiveData<List<NetworkGroup>>
    get() = _groups

  init {
    getGroups(quizId)
  }

  private fun getGroups(quizId: Long) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _groups.value = repository.getGroupsOfQuiz(quizId)
      } catch (e: Exception) {
        _groups.value = ArrayList()
        when (e) {
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
}
