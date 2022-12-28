package com.zhiyong.tingxie.ui.group_membership

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkGroup
import com.zhiyong.tingxie.viewmodel.Status

class GroupMembershipViewModel(application: Application, email: String) :
  AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _groups = MutableLiveData<List<NetworkGroup>>()
  val groups: LiveData<List<NetworkGroup>>
    get() = _groups

  init {
    getGroups(email)
  }

  private fun getGroups(email: String) {
    // stopped here
  }
}
