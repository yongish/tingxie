package com.zhiyong.tingxie.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.db.Question
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.getDatabase
import com.zhiyong.tingxie.ui.word.WordItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: QuizRepository = QuizRepository(getDatabase(application), -1)
    val allQuizItems: LiveData<List<QuizItem>> = mRepository.allQuizItems
    val allQuizPinyins: LiveData<List<QuizPinyin>> = mRepository.allQuizPinyins
    val allQuestions: LiveData<List<Question>> = mRepository.allQuestions

    fun insertQuiz(quiz: Quiz?) {
        viewModelScope.launch (Dispatchers.IO) {
            mRepository.insertQuiz(quiz)
        }
    }

    fun addWords(quizId: Long, wordItems: List<WordItem>) {
        for (wordItem in wordItems) {
            mRepository.addWord(quizId, wordItem.wordString, wordItem.pinyinString)
        }
    }

    fun updateQuiz(quiz: Quiz?) {
        mRepository.updateQuiz(quiz)
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

    fun insertQuizPinyins(quizPinyins: List<QuizPinyin?>) {
        for (quizPinyin in quizPinyins) {
            mRepository.insertQuizPinyin(quizPinyin)
        }
    }
}