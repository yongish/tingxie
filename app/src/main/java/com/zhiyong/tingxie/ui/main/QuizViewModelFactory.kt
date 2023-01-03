package com.zhiyong.tingxie.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zhiyong.tingxie.Repository

@Suppress("UNCHECKED_CAST")
class QuizViewModelFactory(private val repository: Repository) :
  ViewModelProvider.NewInstanceFactory() {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return QuizViewModel(repository) as T
  }
}
