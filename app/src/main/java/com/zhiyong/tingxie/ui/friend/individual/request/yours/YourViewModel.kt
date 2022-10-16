package com.zhiyong.tingxie.ui.friend.individual.request.yours

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.ui.friend.individual.FriendStatus
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

// may deduplicate with FriendIndividualViewModel.
class YourViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _requests = MutableLiveData<List<TingXieIndividual>>()
  val requests: LiveData<List<TingXieIndividual>>
    get() = _requests

  init {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _requests.value = repository.getFriends(FriendStatus.REQUEST.name)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _requests.value = arrayListOf()
        _status.value = Status.ERROR
      }
    }
  }

  fun addRequest(request: TingXieIndividual) =
    viewModelScope.launch { repository.addFriend(request) }

  fun deleteRequest(email: String) {
    viewModelScope.launch { repository.deleteYourIndividualRequest(email) }
  }
}
