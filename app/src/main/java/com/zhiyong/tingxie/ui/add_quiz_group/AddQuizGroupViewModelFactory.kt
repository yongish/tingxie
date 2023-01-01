package com.zhiyong.tingxie.ui.add_quiz_group

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddQuizGroupViewModelFactory(
  private val application: Application,
  private val email: String
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return AddQuizGroupViewModel(application, email) as T
  }
}
