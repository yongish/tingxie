package com.zhiyong.tingxie.ui.answer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.db.Question
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.ui.main.QuizItem
import com.zhiyong.tingxie.ui.word.WordItem

class AnswerViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: QuizRepository = QuizRepository(-1, application)

    fun resetAsked(quizId: Long) {
        mRepository.resetAsked(quizId)
    }

    fun updateQuiz(quizItem: QuizItem) {
        mRepository.updateQuiz(Quiz(
                quizItem.id,
                quizItem.date,
                quizItem.title,
                quizItem.totalWords,
                quizItem.notLearned,
                quizItem.round
        ))
    }

    fun onAnswer(question: Question, wordItem: WordItem) {
      mRepository.insertQuestion(question)
      mRepository.updateQuizPinyin(QuizPinyin(wordItem.quizId, wordItem.pinyinString, wordItem.wordString, wordItem.isAsked))
    }
}