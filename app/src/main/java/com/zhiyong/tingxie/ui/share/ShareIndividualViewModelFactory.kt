package com.zhiyong.tingxie.ui.share

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShareIndividualViewModelFactory(
  private val mApplication: Application,
  private val groupId: Long
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ShareIndividualViewModel(mApplication, groupId) as T
  }
}
