package com.zhiyong.tingxie.ui.friend.individual.request.yours

import android.app.Application
import android.util.Log
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

// may deduplicate with FriendIndividualViewModel.
class YourViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _requests = MutableLiveData<List<TingXieIndividual>>()
  val requests: LiveData<List<TingXieIndividual>>
    get() = _requests

  private val _shouldReopen = MutableLiveData<Boolean>()
  val shouldReopen: LiveData<Boolean>
    get() = _shouldReopen

  init {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _requests.value =
          repository.getFriends(Party.SELF.name, FriendStatus.REQUEST.name)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _requests.value = arrayListOf()
        _status.value = Status.ERROR
      }
    }
  }

  fun checkIfShouldReopen(email: String) = viewModelScope.launch {
    try {
      val userExists = repository.checkUserExists(email)
      _shouldReopen.value = !userExists
      if (userExists) {
        repository.addFriend(TingXieIndividual(email, "", FriendStatus.REQUEST.name))
        _requests.value =
          repository.getFriends(Party.SELF.name, FriendStatus.REQUEST.name)
      }
    } catch (e: NoSuchElementException) {
      _shouldReopen.value = true
    } catch (e: Exception) {
      Log.e("FIVM cISR", e.message.toString())
    }
  }

  fun closeAddFriendModal() {
    _shouldReopen.value = false
  }

  fun addRequest(request: TingXieIndividual) =
    viewModelScope.launch { repository.addFriend(request) }

  fun deleteRequest(email: String) =
    viewModelScope.launch { repository.deleteFriend(email) }

  fun refreshRequests(swipeRefreshLayout: SwipeRefreshLayout) =
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _requests.value =
          repository.getFriends(Party.SELF.name, FriendStatus.REQUEST.name)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _requests.value = arrayListOf()
        _status.value = Status.ERROR
      } finally {
        swipeRefreshLayout.isRefreshing = false
      }
    }
}
