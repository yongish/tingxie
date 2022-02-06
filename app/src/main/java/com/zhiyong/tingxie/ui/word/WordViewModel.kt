package com.zhiyong.tingxie.ui.word

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.ui.main.QuizItem
import kotlinx.coroutines.launch
import java.io.IOException

internal class WordViewModel(application: Application, quizId: Long) : AndroidViewModel(application) {
    val wordItemsOfQuiz: LiveData<List<WordItem>>
    val quizItem: LiveData<QuizItem>
    private val mRepository: QuizRepository = QuizRepository(application)

    private var _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        wordItemsOfQuiz = mRepository.getWordItemsOfQuiz(quizId)
        quizItem = mRepository.getQuizItem(quizId)
    }

    fun addWord(quizId: Long, word: String?, pinyin: String?) {
        mRepository.addWord(quizId, word, pinyin)
    }

    fun deleteWord(quizPinyin: QuizPinyin?) {
        mRepository.deleteWord(quizPinyin)
    }

    fun addQuizPinyin(quizPinyin: QuizPinyin?) {
        mRepository.insertQuizPinyin(quizPinyin)
    }

    fun updateQuestions(quizId: Long) {
        mRepository.updateQuestions(quizId)
    }

    fun updateQuiz(quiz: Quiz) {
        mRepository.updateQuiz(quiz)
    }

    fun refreshWordItemsOfQuiz(wordItems: List<WordItem>) {
        viewModelScope.launch {
            try {
                mRepository.refreshWordItemsOfQuiz(wordItems)
            } catch (networkError: IOException) {
                if (wordItemsOfQuiz.value.isNullOrEmpty()) {
                    _eventNetworkError.value = true
                }
            }
        }
    }
}
