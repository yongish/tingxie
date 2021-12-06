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

class GroupViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _groups = MutableLiveData<List<TingXieGroup>>()
  val groups: LiveData<List<TingXieGroup>>
    get() = _groups

  init {
    val email = FirebaseAuth.getInstance().currentUser?.email
    if (email == null) {
      FirebaseCrashlytics.getInstance().recordException(Exception("NO EMAIL."))
    } else {
      getGroups(email)
    }
  }

  private fun getGroups(email: String) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _groups.value = repository.getGroups(email)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _groups.value = ArrayList()
        _status.value = Status.ERROR
      }
    }
  }

  fun addGroup(group: TingXieGroup) =
      viewModelScope.launch { repository.addGroup(group) }

  fun updateGroup(group: TingXieGroup) =
      viewModelScope.launch { repository.updateGroup(group) }

  fun deleteGroup(name: String) = viewModelScope.launch {
    val email = FirebaseAuth.getInstance().currentUser?.email
    if (email == null) {
      FirebaseCrashlytics.getInstance().recordException(Exception("NO EMAIL."))
    } else {
      repository.deleteGroup(email, name)
    }
  }
}
