package com.zhiyong.tingxie.ui.friend.individual.request.others

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

class OtherViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _requests = MutableLiveData<List<TingXieOtherIndividualRequest>>()
  val requests: LiveData<List<TingXieOtherIndividualRequest>>
    get() = _requests

  init {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _requests.value = repository.getOtherIndividualRequests()
        _status.value = Status.DONE
      } catch (e: Exception) {
        _requests.value = arrayListOf()
        _status.value = Status.ERROR
      }
    }
  }

  fun acceptRequest(email: String) {
    viewModelScope.launch { repository.acceptOtherIndividualRequest(email) }
  }

  fun rejectRequest(email: String) {
    viewModelScope.launch { repository.rejectOtherIndividualRequest(email) }
  }


}