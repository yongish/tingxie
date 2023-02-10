package com.zhiyong.tingxie.ui.readings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReadingsViewModelFactory(private val application: Application, private val email: String) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ReadingsViewModel(application, email) as T
  }
}