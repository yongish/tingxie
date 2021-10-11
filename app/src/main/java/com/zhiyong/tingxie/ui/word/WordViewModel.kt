package com.zhiyong.tingxie.ui.word

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.ui.main.QuizItem

internal class WordViewModel(application: Application, quizId: Long) : AndroidViewModel(application) {
    val wordItemsOfQuiz: LiveData<List<WordItem>>
    val quizItem: LiveData<QuizItem>
    private val mRepository: QuizRepository = QuizRepository(quizId, application)

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

    init {
        wordItemsOfQuiz = mRepository.wordItemsOfQuiz
        quizItem = mRepository.quizItem
    }
}
