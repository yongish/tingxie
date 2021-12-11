package com.zhiyong.tingxie.ui.share

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShareGroupViewModelFactory(val application: Application, val quizId: Long) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(ShareGroupNameViewModel::class.java)) {
      return ShareGroupNameViewModel(application, quizId) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}