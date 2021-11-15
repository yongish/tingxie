package com.zhiyong.tingxie.ui.friends

import android.app.Application
import androidx.lifecycle.*
import com.zhiyong.tingxie.QuizRepository
import kotlinx.coroutines.launch

enum class Status { LOADING, ERROR, DONE }

class FriendsViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _friends = MutableLiveData<List<TingXieFriend>>()
  val friends: LiveData<List<TingXieFriend>>
    get() = _friends

  init {
    getFriends()
  }

  private fun getFriends() {
    _status.value = Status.LOADING
    viewModelScope.launch {
      try {
        _friends.value = repository.getFriends()
        _status.value = Status.DONE
      } catch (e: Exception) {
        _friends.value = ArrayList()
        _status.value = Status.ERROR
      }
    }
  }
}
