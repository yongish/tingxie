package com.zhiyong.tingxie.ui.question

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.ui.word.WordItem

class QuestionViewModel(application: Application, quizId: Long) : AndroidViewModel(application) {
    private val mRepository: QuizRepository = QuizRepository(quizId, application)
    val wordItemsOfQuiz: LiveData<List<WordItem>> = mRepository.wordItemsOfQuiz
    val remainingQuestions: LiveData<List<WordItem>> = mRepository.remainingQuestionsOfQuiz

    fun resetAsked(quizId: Long) = mRepository.resetAsked(quizId)
}