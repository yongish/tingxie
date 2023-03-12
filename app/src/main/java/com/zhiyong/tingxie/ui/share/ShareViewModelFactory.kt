package com.zhiyong.tingxie.ui.share

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShareViewModelFactory(
  private val mApplication: Application,
  private val groupId: Long
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ShareViewModel(mApplication, groupId) as T
  }
}
