package com.zhiyong.tingxie.ui.share

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.ui.friends.Status

class ShareViewModel(application: Application, quizId: Long) : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _roles = MutableLiveData<List<TingXieQuizRole>>()
  val roles: LiveData<List<TingXieQuizRole>>
    get() = _roles

  init {
    getRoles(quizId)
  }

  private fun getRoles(quizId: Long) {

  }
}
