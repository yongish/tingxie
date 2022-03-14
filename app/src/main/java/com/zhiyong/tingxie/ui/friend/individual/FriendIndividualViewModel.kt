package com.zhiyong.tingxie.ui.friend.individual

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

data class AddFriendStatus(
    val modalOpen: Boolean, val friendExists: Boolean, val status: Status
    )

open class FriendIndividualViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _friends = MutableLiveData<List<TingXieIndividual>>()
  val friends: LiveData<List<TingXieIndividual>>
    get() = _friends

//  private val _addFriendStatus = MutableLiveData<AddFriendStatus>()
//  val addFriendStatus: LiveData<AddFriendStatus>
//    get() = _addFriendStatus
  private val _shouldReopen = MutableLiveData<Boolean>()
  val shouldReopen: LiveData<Boolean>
    get() = _shouldReopen

  init {
    getIndividuals()
//    _addFriendStatus.value = AddFriendStatus(false, false, Status.DONE)
    _shouldReopen.value = false
  }

  private fun getIndividuals() {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _friends.value = repository.getFriends()
        _status.value = Status.DONE
      } catch (e: Exception) {
        _friends.value = ArrayList()
        when(e) {
          is NoSuchElementException -> _status.value = Status.DONE
          else -> _status.value = Status.ERROR
        }
      }
    }
  }

  fun checkIfShouldReopen(email: String) {
    viewModelScope.launch {
      try {
        _shouldReopen.value = !repository.checkUserExists(email)
      } catch (e: NoSuchElementException) {
        _shouldReopen.value = true
      }
    }
  }

  fun closeAddFriendModal() {
    _shouldReopen.value = false
  }

  fun addIndividual(individual: TingXieIndividual) {
    viewModelScope.launch {
      repository.addFriend(individual)
    }
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
}
