package com.zhiyong.tingxie.ui.share

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

class ShareGroupNameViewModel(application: Application, quizId: Long)
  : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _groups = MutableLiveData<List<TingXieShareGroup>>()
  val groups: LiveData<List<TingXieShareGroup>>
    get() = _groups

  init {
    getShareGroups(quizId)
  }

  private fun getShareGroups(quizId: Long) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _groups.value = repository.getShareGroups(quizId)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _groups.value = ArrayList()
        _status.value = Status.ERROR
      }
    }
  }

  fun addShareGroup(quizId: Long, name: String) =
      viewModelScope.launch { repository.addShareGroup(quizId, name) }

  fun deleteShareGroup(quizId: Long, name: String) =
      viewModelScope.launch { repository.deleteShareGroup(quizId, name) }
}
