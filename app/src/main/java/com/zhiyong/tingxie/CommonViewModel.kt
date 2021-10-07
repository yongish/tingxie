package com.zhiyong.tingxie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.zhiyong.tingxie.db.QuizPinyin

// todo: Try placing duplicate functions of QuizViewMode and WordViewModel.
abstract class CommonViewModel(application: Application, quizId: Int) : AndroidViewModel(application) {
    var repository: QuizRepository
        protected set

//    fun addQuizPinyin(quizPinyin: QuizPinyin?) {
//        repository.insertQuizPinyin(quizPinyin)
//    }

    init {
        repository = QuizRepository(quizId.toLong(), application)
    }
}