package com.zhiyong.tingxie.ui.share

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkAddQuizUser
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShareViewModel(application: Application, quizId: Long) :
  AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  val currentUser = FirebaseAuth.getInstance().currentUser!!
  val creatorEmail = currentUser.email!!
  val creatorName = currentUser.displayName!!

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _users = MutableLiveData<List<NetworkGroupMember>>()
  val users: LiveData<List<NetworkGroupMember>>
    get() = _users

  init {
    getUsers(quizId)
  }

  private fun getUsers(quizId: Long) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _users.value = repository.getUsersOfQuiz(quizId)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _users.value = ArrayList()
        when (e) {
          is NoSuchElementException -> {
            _status.value = Status.DONE
          }
          else -> {
            Toast.makeText(getApplication(), e.message, Toast.LENGTH_LONG).show()
            _status.value = Status.ERROR
          }
        }
      }
    }
  }

  fun changeRole(quizId: Long, email: String, role: String): LiveData<Int> {
    val result = MutableLiveData<Int>()
    viewModelScope.launch(Dispatchers.IO) {
      val numRows = repository.changeQuizRole(
        quizId,
        NetworkAddQuizUser(creatorName, creatorEmail, email, role)
      )
      result.postValue(numRows)
    }
    return result
  }

//  fun addShare(quizId: Long, shareIndividual: TingXieShareIndividual) {
//    viewModelScope.launch { repository.addShare(quizId, shareIndividual) }
//  }

//  fun setAllShared(quizId: Long, shared: Boolean) {
//    viewModelScope.launch {
//      _status.value = Status.LOADING
//      try {
//        _shares.value = repository.shareAll(quizId, shared)
//        _status.value = Status.DONE
//      } catch (e: Exception) {
//        _shares.value = ArrayList()
//        _status.value = Status.ERROR
//      }
//    }
//  }

  fun removeQuizMember(
    quizId: Long,
    requesterName: String?,
    requesterEmail: String?,
    email: String
  ): LiveData<String> {
    val result = MutableLiveData<String>()
    if (requesterName != null && requesterEmail != null) {
      viewModelScope.launch(Dispatchers.IO) {
        val numRows =
          repository.removeQuizMember(quizId, requesterEmail, requesterEmail, email)
        result.postValue(numRows)
      }
    }
    return result
  }

  // todo: DELETE THIS.
  fun deleteShare(quizId: Long, email: String) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        repository.deleteShare(quizId, email)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _status.value = Status.ERROR
      }
    }
  }
}
