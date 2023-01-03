package com.zhiyong.tingxie.ui.word

import com.zhiyong.tingxie.Repository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class WordViewModelFactory(
  private val repository: Repository,
  private val mQuizId: Long
) : ViewModelProvider.NewInstanceFactory() {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return WordViewModel(repository, mQuizId) as T
  }
}
