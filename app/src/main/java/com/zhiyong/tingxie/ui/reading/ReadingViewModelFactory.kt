package com.zhiyong.tingxie.ui.reading

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReadingViewModelFactory(private val application: Application, private val id: Long) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ReadingViewModel(application, id) as T
  }
}