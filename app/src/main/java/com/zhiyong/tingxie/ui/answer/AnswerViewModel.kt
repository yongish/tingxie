package com.zhiyong.tingxie.ui.answer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.db.Question
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.getDatabase
import com.zhiyong.tingxie.ui.main.QuizItem
import com.zhiyong.tingxie.ui.word.WordItem

class AnswerViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: QuizRepository = QuizRepository(getDatabase(application), -1)
    fun insertQuestion(question: Question?) {
        mRepository.insertQuestion(question)
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

    fun updateWordItem(wordItem: WordItem) {
        // todo: Can WordItem and QuizPinyin be the same class?
        mRepository.updateQuizPinyin(QuizPinyin(wordItem.quizId, wordItem.pinyinString, wordItem.wordString, wordItem.isAsked))
    }
}