package com.zhiyong.tingxie.ui.friend.individual.request.others

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.ui.friend.individual.FriendStatus
import com.zhiyong.tingxie.ui.friend.individual.Party
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

class OtherViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = QuizRepository(application)

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
        _requests.value =
          repository.getFriends(Party.OTHERS.name, FriendStatus.REQUEST.name)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _requests.value = arrayListOf()
        _status.value = Status.ERROR
      }
    }
  }

  fun acceptRequest(request: TingXieIndividual) =
    viewModelScope.launch {
      repository.updateFriend(
        TingXieIndividual(
          request.email,
          request.name,
          "FRIEND"
        )
      )
      _requests.value =
        repository.getFriends(Party.OTHERS.name, FriendStatus.REQUEST.name)
    }

  fun rejectRequest(email: String) =
    viewModelScope.launch { repository.deleteFriend(email) }

  fun refreshRequests(swipeRefreshLayout: SwipeRefreshLayout) =
    viewModelScope.launch {
      try {
        _requests.value =
          repository.getFriends(Party.OTHERS.name, FriendStatus.REQUEST.name)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _requests.value = arrayListOf()
        _status.value = Status.ERROR
      } finally {
        swipeRefreshLayout.isRefreshing = false
      }
    }
}
