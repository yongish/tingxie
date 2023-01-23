package com.zhiyong.tingxie.ui.exercise_type

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkExerciseType
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

class ExerciseTypeViewModel(application: Application, gradeLevel: Int, email: String) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _exerciseTypes = MutableLiveData<List<NetworkExerciseType>>()
  val exerciseTypes: LiveData<List<NetworkExerciseType>>
    get() = _exerciseTypes

  init {
    getExerciseTypes(gradeLevel, email)
  }

  private fun getExerciseTypes(gradeLevel: Int, email: String) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _exerciseTypes.value = repository.getExerciseTypes(gradeLevel, email)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _exerciseTypes.value = ArrayList()
        when (e) {
          is NoSuchElementException -> {
            _status.value = Status.DONE
          }
          else -> {
            Toast.makeText(getApplication(), e.message, Toast.LENGTH_LONG).show()
            _status.value = Status.ERROR
          }
        }
      }
    }
  }
}
