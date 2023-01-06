package com.zhiyong.tingxie.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.Repository
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.network.NetworkQuiz
import com.zhiyong.tingxie.ui.word.WordItem
import com.zhiyong.tingxie.viewmodel.Status
import com.zhiyong.tingxie.viewmodel.UpdateQuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizViewModel(private val mRepository: Repository) :
  UpdateQuizViewModel(mRepository) {
  // todo: After implementing fetch from remote, use these to update remote DB.
  // Have a flag to check if remote DB has been successfully updated.
//    val allQuizItems: LiveData<List<QuizItem>> = mRepository.quizzes
//    val allQuizPinyins: LiveData<List<QuizPinyin>> = mRepository.allQuizPinyins
//    val allQuestions: LiveData<List<Question>> = mRepository.allQuestions

  private var _eventNetworkError = MutableLiveData(false)
  val eventNetworkError: LiveData<Boolean>
    get() = _eventNetworkError

  private var _isNetworkErrorShown = MutableLiveData(false)
  val isNetworkErrorShown: LiveData<Boolean>
    get() = _isNetworkErrorShown

  fun putToken(
    uid: String,
    email: String,
    token: String,
  ) {
    viewModelScope.launch {
      mRepository.putToken(uid, email, token)
    }
  }

  private val _quizItems = MutableLiveData<List<NetworkQuiz>>()
  val allQuizItems: LiveData<List<NetworkQuiz>> = _quizItems

  private val _quizzesStatus = MutableLiveData<Status>()
  val quizzesStatus: LiveData<Status> = _quizzesStatus

  init {
    getQuizzes()
  }

  private fun getQuizzes() {
    viewModelScope.launch {
      _quizzesStatus.value = Status.LOADING
      try {
        _quizItems.value = mRepository.getQuizzes()
        _quizzesStatus.value = Status.DONE
      } catch (e: Exception) {
        _quizzesStatus.value = Status.ERROR
        _quizItems.value = listOf()
      }
    }
  }

  fun createQuiz(title: String, date: Int): LiveData<Long> {
    val result = MutableLiveData<Long>()
    viewModelScope.launch(Dispatchers.IO) {
      val newId = mRepository.createQuiz(title, date)
      result.postValue(newId)
    }
    return result
  }

  fun onNetworkErrorShown() {
    _isNetworkErrorShown.value = true
  }

//    fun insertQuiz(quiz: Quiz?) {
//        viewModelScope.launch (Dispatchers.IO) {
//            mRepository.insertQuiz(quiz)
//        }
//    }

//    fun insertQuizFuture(quiz: Quiz?): Long {
//        val quizIdFuture = mRepository.insertQuizFuture(quiz)
//        try {
//            return quizIdFuture.get()
//        } catch (e: Exception) {
//            Log.e("insertQuiz1", "error")
//        }
//        return -2
//    }

  fun addWords(quizId: Long, wordItems: List<WordItem>) {
    for (wordItem in wordItems) {
      viewModelScope.launch {
        mRepository.addWord(quizId, wordItem.wordString, wordItem.pinyinString)
      }
    }
  }

  fun deleteQuiz(quizId: Long) {
    viewModelScope.launch(Dispatchers.IO) {
      mRepository.deleteQuiz(quizId)

      // todo: 12/1/22. Deleting a quiz remotely no longer deletes all the words.
//            mRepository.deleteQuizPinyins(quizId)
    }
  }

  fun insertQuizPinyin(quizPinyin: QuizPinyin) {
    mRepository.insertQuizPinyin(quizPinyin)
  }
}
