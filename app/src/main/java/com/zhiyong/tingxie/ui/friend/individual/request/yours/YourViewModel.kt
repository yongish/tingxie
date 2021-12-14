package com.zhiyong.tingxie.ui.friend.individual.request.yours

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

class YourViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _requests = MutableLiveData<List<TingXieYourIndividualRequest>>()
  val requests: LiveData<List<TingXieYourIndividualRequest>>
    get() = _requests

  init {
    getRequests()
  }

  private fun getRequests() {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _requests.value = repository.getYourIndividualRequests()
        _status.value = Status.DONE
      } catch (e: Exception) {
        _requests.value = arrayListOf()
        _status.value = Status.ERROR
      }
    }
  }

  fun addRequest(request: TingXieYourIndividualRequest) {
    viewModelScope.launch { repository.addYourIndividualRequest(request) }
  }

  fun deleteRequest(email: String) {
    viewModelScope.launch { repository.deleteYourIndividualRequest(email) }
  }
}
