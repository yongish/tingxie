package com.zhiyong.tingxie.ui.group_member

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.viewmodel.CrudStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupMemberViewModel(application: Application, groupId: Long) :
  AndroidViewModel(application) {
  private val mRepository: QuizRepository = QuizRepository(application)

  private val _groupMembers = MutableLiveData<List<NetworkGroupMember>>()
  val groupMembers: LiveData<List<NetworkGroupMember>> = _groupMembers

  private val _groupMembersStatus = MutableLiveData<CrudStatus>()
  val groupMembersStatus: LiveData<CrudStatus> = _groupMembersStatus

  init {
    getGroupMembers(groupId)
  }

  private fun getGroupMembers(groupId: Long) =
    viewModelScope.launch {
      _groupMembersStatus.value = CrudStatus.LOADING
      try {
        _groupMembers.value = mRepository.getGroupMembers(groupId)
        _groupMembersStatus.value = CrudStatus.DONE
      } catch (e: Exception) {
        _groupMembers.value = listOf()
        _groupMembersStatus.value = CrudStatus.ERROR
      }
    }

  fun addUser(groupId: Long, userProps: NetworkGroupMember): LiveData<Long> {
    val result = MutableLiveData<Long>()
    viewModelScope.launch(Dispatchers.IO) {
      val newId = mRepository.addUserToGroup(groupId, userProps)
      result.postValue(newId)
    }
    return result
  }

  fun deleteGroupMember(groupId: Long, requesterName: String, requesterEmail: String, email: String): LiveData<Int> {
    val result = MutableLiveData<Int>()
    viewModelScope.launch(Dispatchers.IO) {
      val numRows = mRepository.deleteGroupMember(groupId, requesterName, requesterEmail, email)
      result.postValue(numRows)
    }
    return result
  }

  fun deleteGroup(groupId: Long, requesterName: String, requesterEmail: String): LiveData<Int> {
    val result = MutableLiveData<Int>()
    viewModelScope.launch(Dispatchers.IO) {
      val numRows = mRepository.deleteGroup(groupId, requesterName, requesterEmail)
      result.postValue(numRows)
    }
    return result
  }
}
