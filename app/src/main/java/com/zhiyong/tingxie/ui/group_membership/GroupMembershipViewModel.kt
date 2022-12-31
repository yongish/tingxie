package com.zhiyong.tingxie.ui.group_membership

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkGroup
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupMembershipViewModel(application: Application, email: String) :
  AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _groups = MutableLiveData<MutableList<NetworkGroup>>()
  val groups: LiveData<MutableList<NetworkGroup>>
    get() = _groups

  init {
    getGroups(email)
  }

  private fun getGroups(email: String) {
    // stopped here
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _groups.value = repository.getGroups(email)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _groups.value = ArrayList()
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

  fun createGroup(groupName: String, members: List<NetworkGroupMember>): LiveData<String> {
    val result = MutableLiveData<String>()
    viewModelScope.launch(Dispatchers.IO) {
      val newId = repository.createGroup(groupName, members)
      result.postValue(newId)
    }
    return result
  }
}
