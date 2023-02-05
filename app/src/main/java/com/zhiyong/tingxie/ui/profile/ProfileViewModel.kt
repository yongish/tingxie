package com.zhiyong.tingxie.ui.profile

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkProfile
import com.zhiyong.tingxie.viewmodel.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application, email: String) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _profile = MutableLiveData<NetworkProfile>()
  val profile: LiveData<NetworkProfile>
    get() = _profile

  init {
    getProfile(email)
  }

  private fun getProfile(email: String) {
    viewModelScope.launch {
      _status.value = Status.LOADING
      try {
        _profile.value = repository.getProfile(email)
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

  fun putProfile(email: String, profile: NetworkProfile): LiveData<String> {
    val result = MutableLiveData<String>()
    viewModelScope.launch(Dispatchers.IO) {
      val response = repository.putProfile(email, profile)
      result.postValue(response)
    }
    return result
  }
}