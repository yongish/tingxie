package com.zhiyong.tingxie.ui.word

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.viewmodel.CrudStatus
import kotlinx.coroutines.launch

internal class WordViewModel(application: Application, quizId: Long) : AndroidViewModel(application) {
//    val wordItemsOfQuiz: LiveData<List<WordItem>>
    private val mRepository: QuizRepository = QuizRepository(application)

    private var _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private val _wordItemsOfQuiz = MutableLiveData<List<WordItem>>()
    val wordItemsOfQuiz: LiveData<List<WordItem>> = _wordItemsOfQuiz

    private val _wordItemsOfQuizStatus = MutableLiveData<CrudStatus>()
    val wordItemsOfQuizStatus: LiveData<CrudStatus> = _wordItemsOfQuizStatus

    init {
        getWordItemsOfQuiz(quizId)
    }

    private fun getWordItemsOfQuiz(quizId: Long) {
        viewModelScope.launch {
            _wordItemsOfQuizStatus.value = CrudStatus.LOADING
            try {
                _wordItemsOfQuiz.value = mRepository.getWordItemsOfQuiz(quizId)
                _wordItemsOfQuizStatus.value = CrudStatus.DONE
            } catch (e: Exception) {
                _wordItemsOfQuizStatus.value = CrudStatus.ERROR
                _wordItemsOfQuiz.value = listOf()
            }
        }
    }

    fun addWord(quizId: Long, word: String?, pinyin: String?) {
        viewModelScope.launch {
            _wordItemsOfQuizStatus.value = CrudStatus.LOADING
            try {

                val id = mRepository.addWord(quizId, word, pinyin)
                _wordItemsOfQuiz.value =
                    _wordItemsOfQuiz.value?.plus(WordItem(id, quizId, word, pinyin))

                _wordItemsOfQuizStatus.value = CrudStatus.DONE
            } catch (e: Exception) {
                _wordItemsOfQuizStatus.value = CrudStatus.ERROR_CREATE
                _wordItemsOfQuiz.value = listOf()
            }
        }

    }

    fun deleteWord(wordItem: WordItem) {
        viewModelScope.launch {
            _wordItemsOfQuizStatus.value = CrudStatus.LOADING
            try {
                mRepository.deleteWord(wordItem.id)
                _wordItemsOfQuiz.value = _wordItemsOfQuiz.value?.minus(wordItem)
            } catch (e: Exception) {
                _wordItemsOfQuizStatus.value = CrudStatus.ERROR_DELETE
            }
        }
    }

//    fun addQuizPinyin(quizPinyin: QuizPinyin?) {
//        mRepository.insertQuizPinyin(quizPinyin)
//    }

    fun updateQuestions(quizId: Long) {
        mRepository.updateQuestions(quizId)
    }

    fun updateQuiz(quiz: Quiz) {
        mRepository.updateQuiz(quiz)
    }

//    fun refreshWordItemsOfQuiz(quizId: Long, wordItems: List<WordItem>) {
//        viewModelScope.launch {
//            try {
//                mRepository.refreshWordItemsOfQuiz(quizId, wordItems)
//                _eventNetworkError.value = false
//                _isNetworkErrorShown.value = false
//            } catch (networkError: IOException) {
//                if (wordItemsOfQuiz.value.isNullOrEmpty()) {
//                    _eventNetworkError.value = true
//                }
//            }
//        }
//    }
}
