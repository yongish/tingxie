package com.zhiyong.tingxie.ui.friend.group.member

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.ui.friend.individual.FriendIndividualViewModel

class FriendGroupMemberViewModel(application: Application) : FriendIndividualViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _members = MutableLiveData<List<TingXieGroupMember>>()
  val members: LiveData<List<TingXieGroupMember>>
    get() = _members

  fun add(groupName: String, member: TingXieGroupMember) {

  }

  fun delete(groupName: String, email: String) {

  }
}