package com.zhiyong.tingxie.ui.friends

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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
//    getFriends()
    val email = FirebaseAuth.getInstance().currentUser?.email
    if (email != null) {
      getFriends(email)
    }
  }

  private fun getFriends(email: String) {
    _status.value = Status.LOADING
    viewModelScope.launch {
      try {
        _friends.value = repository.getFriends(email)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _friends.value = ArrayList()
        _status.value = Status.ERROR
      }
    }
  }

  fun addFriend(friend: TingXieFriend) {
    viewModelScope.launch {
      repository.addFriend(friend)
    }
  }

  fun deleteFriend(email: String) {
    viewModelScope.launch {
      try {
        repository.deleteFriend(email)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _status.value = Status.ERROR
      }
    }
  }
}
