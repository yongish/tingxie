package com.zhiyong.tingxie.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.db.Question
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.ui.word.WordItem
import com.zhiyong.tingxie.viewmodel.UpdateQuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class QuizViewModel(application: Application) : UpdateQuizViewModel(application) {
    val allQuizItems: LiveData<List<QuizItem>> = mRepository.quizzes
    val allQuizPinyins: LiveData<List<QuizPinyin>> = mRepository.allQuizPinyins
    val allQuestions: LiveData<List<Question>> = mRepository.allQuestions

    private var _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private val repository = QuizRepository(application)
    val quizzes = repository.quizzes

    fun putToken(uid: String, email: String, token: String) {
        viewModelScope.launch {
            repository.putToken(uid, email, token)
        }
    }

    fun refreshQuizzes(quizItems: List<QuizItem>) {
        viewModelScope.launch {
            try {
                repository.refreshQuizzes(quizItems)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                if (quizzes.value.isNullOrEmpty()) {
                    _eventNetworkError.value = true
                }
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    fun insertQuiz(quiz: Quiz?) {
        viewModelScope.launch (Dispatchers.IO) {
            mRepository.insertQuiz(quiz)
        }
    }

    fun insertQuizFuture(quiz: Quiz?): Long {
        val quizIdFuture = mRepository.insertQuizFuture(quiz)
        try {
            return quizIdFuture.get()
        } catch (e: Exception) {
            Log.e("insertQuiz1", "error")
        }
        return -2
    }

    fun addWords(quizId: Long, wordItems: List<WordItem>) {
        for (wordItem in wordItems) {
            mRepository.addWord(quizId, wordItem.wordString, wordItem.pinyinString)
        }
    }

    fun deleteQuiz(quizId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.deleteQuiz(quizId)
            mRepository.deleteQuizPinyins(quizId)
        }
    }

    fun insertQuestions(questions: List<Question?>) {
        for (question in questions) {
            mRepository.insertQuestion(question)
        }
    }

    fun insertQuizPinyin(quizPinyin: QuizPinyin) {
        mRepository.insertQuizPinyin(quizPinyin)
    }
}
