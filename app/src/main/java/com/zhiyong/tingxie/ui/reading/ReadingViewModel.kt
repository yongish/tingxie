package com.zhiyong.tingxie.ui.reading

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkPassage
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.launch

class ReadingViewModel(application: Application, id: Long) :
  AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _passage = MutableLiveData<NetworkPassage>()
  val passage: LiveData<NetworkPassage>
    get() = _passage

  init {
    getPassage(id)
  }

  private fun getPassage(id: Long) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _passage.value = repository.getReadingPassage(id)
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