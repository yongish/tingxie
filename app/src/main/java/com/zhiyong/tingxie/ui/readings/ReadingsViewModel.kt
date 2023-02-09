package com.zhiyong.tingxie.ui.readings

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkTitle
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

class ReadingsViewModel(application: Application, email: String) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _titles = MutableLiveData<MutableList<NetworkTitle>>()
  val titles: LiveData<MutableList<NetworkTitle>>
    get() = _titles

  init {
    getTitles(email)
  }

  private fun getTitles(email: String) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _titles.value = repository.getReadingTitles(email)
        _status.value = Status.DONE
      } catch (e: Exception) {
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
