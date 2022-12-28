package com.zhiyong.tingxie.ui.group_membership

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GroupMembershipViewModelFactory(
  private val mApplication: Application,
  private val email: String
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return GroupMembershipViewModel(mApplication, email) as T
  }
}
