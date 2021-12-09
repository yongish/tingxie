package com.zhiyong.tingxie.ui.friend

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zhiyong.tingxie.QuizRepository

class GroupMemberViewModel(application: Application) : IndividualViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _members = MutableLiveData<List<TingXieGroupMember>>()
  val members: LiveData<List<TingXieGroupMember>>
    get() = _members

  fun addGroupMember(groupName: String, email: String) {

  }
}