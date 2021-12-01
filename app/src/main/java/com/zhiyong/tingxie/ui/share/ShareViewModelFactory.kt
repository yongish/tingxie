package com.zhiyong.tingxie.ui.share

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShareViewModelFactory(
    private val quizId: Long,private val application: Application
    ) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(ShareViewModel::class.java)) {
      return ShareViewModel(quizId, application) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}