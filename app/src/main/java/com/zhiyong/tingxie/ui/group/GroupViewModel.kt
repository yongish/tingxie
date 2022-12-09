package com.zhiyong.tingxie.ui.group

import android.app.Application
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

class GroupViewModel(application: Application) : AndroidViewModel(application) {
  val repository: QuizRepository = QuizRepository(application)

  init {
    getGroups()
  }

  private val _groups = MutableLiveData<List<NetworkGroup>>()
  val groups: LiveData<List<NetworkGroup>> = _groups

  private val _groupsStatus = MutableLiveData<Status>()
  val groupsStatus: LiveData<Status> = _groupsStatus

  private fun getGroups() {
    viewModelScope.launch {
      _groupsStatus.value = Status.LOADING
      try {
        _groups.value = repository.getGroups()
        _groupsStatus.value = Status.DONE
      } catch (e: Exception) {
        _groups.value = arrayListOf()
        _groupsStatus.value = Status.ERROR
      }
    }
  }

  fun createGroup(groupName: String, members: List<NetworkGroupMember>): LiveData<Long> {
    val result = MutableLiveData<Long>()
    viewModelScope.launch(Dispatchers.IO) {
      val newId = repository.createGroup(groupName, members)
      result.postValue(newId)
    }
    return result
  }

  // todo: This should be in GroupMemberViewModel that will be created soon.
  fun addUser(groupId: Long, userProps: NetworkGroupMember): LiveData<Long> {
    val result = MutableLiveData<Long>()
    viewModelScope.launch(Dispatchers.IO) {
      val newId = repository.addUserToGroup(groupId, userProps)
      result.postValue(newId)
    }
    return result
  }
}
