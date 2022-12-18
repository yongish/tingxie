package com.zhiyong.tingxie.ui.group_member

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.ui.friend.individual.FriendStatus
import com.zhiyong.tingxie.ui.friend.individual.Party
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual
import com.zhiyong.tingxie.viewmodel.CrudStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupMemberViewModel(application: Application, groupId: Long) :
  AndroidViewModel(application) {
  private val mRepository: QuizRepository = QuizRepository(application)

  private val _groupMembers = MutableLiveData<MutableList<NetworkGroupMember>>()
  val groupMembers: LiveData<MutableList<NetworkGroupMember>> = _groupMembers

  private val _groupMembersStatus = MutableLiveData<CrudStatus>()
  val groupMembersStatus: LiveData<CrudStatus> = _groupMembersStatus

  private val _shouldReopen = MutableLiveData<Boolean>()
  val shouldReopen: LiveData<Boolean>
    get() = _shouldReopen

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
        _groupMembers.value = mutableListOf()
        _groupMembersStatus.value = CrudStatus.ERROR
      }
    }

  fun addMemberOrReturnNoUser(
    groupId: Long?,
    email: String
  ): LiveData<NetworkGroupMember> {
    val result = MutableLiveData<NetworkGroupMember>()
    if (groupId != null) {
      viewModelScope.launch(Dispatchers.IO) {
        // It is a security risk to get the details of another user, so we use a single
        // function to check if the user exists, and if so, add the user to the group.
        val newMember = mRepository.addUserToGroup(groupId, email)
        result.postValue(newMember)
      }
    }
    return result;
  }

  fun deleteGroupMember(
    groupId: Long?,
    requesterName: String,
    requesterEmail: String,
    email: String
  ): LiveData<Int> {
    val result = MutableLiveData<Int>()
    if (groupId != null) {
      viewModelScope.launch(Dispatchers.IO) {
        val numRows =
          mRepository.deleteGroupMember(groupId, requesterName, requesterEmail, email)
        result.postValue(numRows)
      }
    }
    return result
  }

  fun deleteGroup(
    groupId: Long?,
    requesterName: String,
    requesterEmail: String
  ): LiveData<Int> {
    val result = MutableLiveData<Int>()
    if (groupId != null) {
      viewModelScope.launch(Dispatchers.IO) {
        val numRows = mRepository.deleteGroup(groupId, requesterName, requesterEmail)
        result.postValue(numRows)
      }
    }
    return result
  }
}
