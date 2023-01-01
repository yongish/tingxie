package com.zhiyong.tingxie.ui.add_quiz_group

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkGroup
import com.zhiyong.tingxie.ui.UserRole
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddQuizGroupViewModel(application: Application, email: String) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _groups = MutableLiveData<MutableList<NetworkGroup>>()
  val groups: LiveData<MutableList<NetworkGroup>>
    get() = _groups

  init {
    getGroups(email)
  }

  private fun getGroups(email: String) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _groups.value = repository.getGroups(email)
        _status.value = Status.DONE
      } catch (e: Exception) {
        _groups.value = ArrayList()
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

  fun addQuizGroup(groupId: Long, userRole: UserRole): LiveData<String> {
    val result = MutableLiveData<String>()
    viewModelScope.launch(Dispatchers.IO) {
      val numRows = repository.addQuizGroup(groupId, userRole)
      result.postValue(numRows)
    }
    return result
  }
}
