package com.zhiyong.tingxie.ui.friend

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

  private val _friends = MutableLiveData<List<TingXieIndividual>>()
  val friends: LiveData<List<TingXieIndividual>>
    get() = _friends

  init {
//    getFriends()
    val email = FirebaseAuth.getInstance().currentUser?.email
    if (email != null) {
      getFriends(email)
    }
  }

  private fun getFriends(email: String) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _friends.value = repository.getFriends(email)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _friends.value = ArrayList()
        _status.value = Status.ERROR
      }
    }
  }

  fun addFriend(individual: TingXieIndividual) {
    viewModelScope.launch {
      repository.addFriend(individual)
    }
  }

  fun deleteFriend(email: String) {
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
}
