package com.zhiyong.tingxie.ui.exercises_completed;

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ExercisesCompletedViewModelFactory(private val application: Application, private val gradeLevel: Int, private val email: String) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ExercisesCompletedViewModel(application, gradeLevel, email) as T
  }
}
