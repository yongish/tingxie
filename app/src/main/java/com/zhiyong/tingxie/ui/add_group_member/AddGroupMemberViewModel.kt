package com.zhiyong.tingxie.ui.add_group_member

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkGroupMember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddGroupMemberViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = QuizRepository(application)

  fun addMemberOrReturnNoUser(
    groupId: Long?,
    email: String,
    role: String
  ): LiveData<NetworkGroupMember> {
    val result = MutableLiveData<NetworkGroupMember>()
    if (groupId != null) {
      viewModelScope.launch(Dispatchers.IO) {
        // It is a security risk to get the details of another user, so we use a single
        // function to check if the user exists, and if so, add the user to the group.
        val newMember = repository.addMemberOrReturnNoUser(groupId, email, role)
        result.postValue(newMember)
      }
    }
    return result;
  }
}
