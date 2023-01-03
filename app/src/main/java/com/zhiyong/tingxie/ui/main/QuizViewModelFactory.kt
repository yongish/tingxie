package com.zhiyong.tingxie.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zhiyong.tingxie.QuizRepositoryInterface

@Suppress("UNCHECKED_CAST")
class QuizViewModelFactory(
  private val mApplication: Application,
  private val repository: QuizRepositoryInterface
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return QuizViewModel(mApplication, repository) as T
  }
}
