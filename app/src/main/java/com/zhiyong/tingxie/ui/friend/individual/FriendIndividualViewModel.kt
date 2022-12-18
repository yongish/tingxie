package com.zhiyong.tingxie.ui.friend.individual

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

enum class FriendStatus {
  REQUEST, FRIEND, BLOCKED
}

enum class Party {
  SELF, OTHERS
}

open class FriendIndividualViewModel(application: Application) :
  AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _friends = MutableLiveData<List<TingXieIndividual>>()
  val friends: LiveData<List<TingXieIndividual>>
    get() = _friends

  private val _shouldReopen = MutableLiveData<Boolean>()
  val shouldReopen: LiveData<Boolean>
    get() = _shouldReopen

  init {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _friends.value = repository.getFriends(Party.SELF.name, FriendStatus.FRIEND.name)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _friends.value = ArrayList()
        when (e) {
          is NoSuchElementException -> _status.value = Status.DONE
          else -> _status.value = Status.ERROR
        }
      }
    }
  }

  fun addIndividual(individual: TingXieIndividual) = viewModelScope.launch {
    repository.addFriend(individual)
  }

  fun deleteIndividual(email: String) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        repository.deleteFriend(email)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _status.value = Status.ERROR
      }
    }
  }

  fun refreshFriends(swipeRefreshLayout: SwipeRefreshLayout) =
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _friends.value = repository.getFriends(Party.SELF.name, FriendStatus.FRIEND.name)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _friends.value = ArrayList()
        when (e) {
          is NoSuchElementException -> _status.value = Status.DONE
          else -> _status.value = Status.ERROR
        }
      } finally {
        swipeRefreshLayout.isRefreshing = false
      }
    }
}
