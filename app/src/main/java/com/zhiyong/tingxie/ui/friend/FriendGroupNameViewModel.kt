package com.zhiyong.tingxie.ui.friend

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.zhiyong.tingxie.QuizRepository
import kotlinx.coroutines.launch

class FriendGroupNameViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _groups = MutableLiveData<List<TingXieGroup>>()
  val groups: LiveData<List<TingXieGroup>>
    get() = _groups

  val email = FirebaseAuth.getInstance().currentUser?.email

  init {
    getGroups()
  }

  private fun getGroups(quizId: Long = -1) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _groups.value = repository.getFriendGroups(quizId)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _groups.value = ArrayList()
        _status.value = Status.ERROR
      }
    }
  }

  fun addGroup(group: TingXieGroup, quizId: Long = -1) =
      viewModelScope.launch { repository.addGroup(group) }

  fun deleteGroup(name: String, quizId: Long = -1) = viewModelScope.launch {
    val email = FirebaseAuth.getInstance().currentUser?.email
    if (email == null) {
      FirebaseCrashlytics.getInstance().recordException(Exception("NO EMAIL."))
    } else {
      repository.deleteGroup(email, name)
    }
  }
}
