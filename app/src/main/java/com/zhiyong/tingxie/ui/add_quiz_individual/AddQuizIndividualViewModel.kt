package com.zhiyong.tingxie.ui.add_quiz_individual

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.network.NetworkAddQuizUser
import com.zhiyong.tingxie.network.NetworkGroupMember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddQuizIndividualViewModel(application: Application) :
  AndroidViewModel(application) {
  private val repository = QuizRepository(application)

  val currentUser = FirebaseAuth.getInstance().currentUser!!
  val creatorEmail = currentUser.email!!
  val creatorName = currentUser.displayName!!

  fun addQuizMemberOrReturnNoUser(
    quizId: Long?,
    email: String,
    role: String
  ): LiveData<NetworkGroupMember> {
    val result = MutableLiveData<NetworkGroupMember>()
    if (quizId != null) {
      viewModelScope.launch(Dispatchers.IO) {
        // It is a security risk to get the details of another user, so we use a single
        // function to check if the user exists, and if so, add the user to the group.
        val newMember = repository.addQuizMemberOrReturnNoUser(
          quizId,
          NetworkAddQuizUser(creatorName, creatorEmail, email, role)
        )
        result.postValue(newMember)
      }
    }
    return result;
  }
}
