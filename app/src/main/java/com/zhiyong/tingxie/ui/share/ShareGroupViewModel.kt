package com.zhiyong.tingxie.ui.share

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.ui.friend.Status
import com.zhiyong.tingxie.ui.friend.TingXieGroup

class ShareGroupViewModel(application: Application, quizId: Long)
  : AndroidViewModel(application) {
  private val repository: QuizRepository = QuizRepository(application)

  private val _status = MutableLiveData<Status>()
  val status: LiveData<Status>
    get() = _status

  private val _groups = MutableLiveData<List<TingXieGroup>>()
  val groups: LiveData<List<TingXieGroup>>
    get() = _groups

  val email = FirebaseAuth.getInstance().currentUser?.email

  init {
    if (email == null) {
      FirebaseCrashlytics.getInstance().recordException(Exception("NO EMAIL."))
    } else {
      getGroups(email, quizId)
    }
  }

  private fun getGroups(email: String, quizId: Long) {

  }
}