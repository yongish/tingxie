package com.zhiyong.tingxie.ui.group_member

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.network.NetworkWordItem
import com.zhiyong.tingxie.viewmodel.CrudStatus
import kotlinx.coroutines.launch

class GroupMemberViewModel(application: Application, groupId: Long) :
  AndroidViewModel(application) {
  private val mRepository: QuizRepository = QuizRepository(application)

  private val _groupMembers = MutableLiveData<List<NetworkGroupMember>>()
  val groupMembers: LiveData<List<NetworkGroupMember>> =
    _groupMembers

  private val _groupMembersStatus = MutableLiveData<CrudStatus>()
  val groupMembersStatus: LiveData<CrudStatus> =
    _groupMembersStatus

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
        _groupMembersStatus.value = CrudStatus.ERROR
      }
    }

  fun deleteGroupMember(groupId: Long, email: String) {
    view
  }
}
