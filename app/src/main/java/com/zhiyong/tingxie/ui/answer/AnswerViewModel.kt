package com.zhiyong.tingxie.ui.answer

import android.app.Application
import com.zhiyong.tingxie.db.Question
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.ui.word.WordItem
import com.zhiyong.tingxie.viewmodel.UpdateQuizViewModel

class AnswerViewModel(application: Application) : UpdateQuizViewModel(application) {
    fun resetAsked(quizId: Long) = mRepository.resetAsked(quizId)

    fun onAnswer(question: Question, wordItem: WordItem) {
      mRepository.insertQuestion(question)
      mRepository.updateQuizPinyin(QuizPinyin(wordItem.quizId, wordItem.pinyinString, wordItem.wordString, wordItem.isAsked))
    }
}