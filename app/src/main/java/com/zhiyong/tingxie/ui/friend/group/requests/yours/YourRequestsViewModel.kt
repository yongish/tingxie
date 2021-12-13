package com.zhiyong.tingxie.ui.friend.group.requests.yours

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

class YourRequestsViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _requests = MutableLiveData<Map<String, Int>>()
  val requests: LiveData<Map<String, Int>>
    get() = _requests

  init {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
//        _requests.value = repository.getYourGroupRequests()
        _status.value = Status.DONE
      } catch (e: Exception) {
        _requests.value = mapOf()
        _status.value = Status.ERROR
      }
    }
  }
}
