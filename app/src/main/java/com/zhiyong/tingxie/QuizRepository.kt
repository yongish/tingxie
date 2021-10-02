package com.zhiyong.tingxie

import android.util.Log
import androidx.lifecycle.LiveData
import com.zhiyong.tingxie.db.Question
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.db.Word
import com.zhiyong.tingxie.ui.main.QuizItem
import com.zhiyong.tingxie.ui.word.WordItem
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/* A Repository is a class that abstracts access to multiple data sources.
A Repository manages query threads and allows you to use multiple backends.
In the most common example, the Repository implements the logic for deciding whether to fetch data
from a network or use results cached in the local database. */
class QuizRepository(database: PinyinRoomDatabase, quizId: Long) {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    private val mQuizDao: QuizDao = database.pinyinDao
    val allQuizItems: LiveData<List<QuizItem>> = mQuizDao.allQuizItems
    val quizItem: LiveData<QuizItem> = mQuizDao.getQuizItem(quizId)
    val wordItemsOfQuiz: LiveData<List<WordItem>> = mQuizDao.getWordItemsOfQuiz(quizId)
    val allQuizPinyins: LiveData<List<QuizPinyin>> = mQuizDao.allQuizPinyins
    val allQuestions: LiveData<List<Question>> = mQuizDao.allQuestions
    val remainingQuestionsOfQuiz: LiveData<List<WordItem>> =
        mQuizDao.getRemainingQuestions(quizId)

    fun remainingHskQuestions(level: Int) {
        // Read appropriate file. It may be more efficient to read a single file into

        //
    }

    fun insertQuiz(quiz: Quiz?) = mQuizDao.insert(quiz)

    fun deleteQuizPinyins(quizId: Long) = mQuizDao.deleteQuizPinyins(quizId)

    fun updateQuiz(quiz: Quiz?) = executor.execute {
        mQuizDao.update(quiz)
    }

    fun insertQuestion(question: Question?) = executor.execute {
        mQuizDao.insert(question)
    }

    fun updateQuestions(quizId: Long?) {
        if (quizId == null) {
            throw IllegalArgumentException("Null quizId.")
        }
        executor.execute {
            mQuizDao.updateQuestions(quizId, System.currentTimeMillis())
        }
    }

    fun addWord(quizId: Long, wordString: String?, pinyinString: String?) {
        // todo: Think Word table is not used anymore can be deleted.
        if (wordString == null || pinyinString == null) {
            throw IllegalArgumentException("Null wordString or pinyinString.")
        }
        executor.execute {
            mQuizDao.insert(Word(wordString, pinyinString))
            mQuizDao.insert(QuizPinyin(quizId, pinyinString, wordString, false))
        }
    }

    // Only delete QuizPinyin object.
    fun deleteWord(quizPinyin: QuizPinyin?) {
        if (quizPinyin == null) {
            throw IllegalArgumentException("Null quizPinyin.")
        }
        executor.execute {
            mQuizDao.deleteQuizPinyin(quizPinyin.quizId, quizPinyin.pinyinString)
        }
    }

    // Undo a just deleted word.
    fun insertQuizPinyin(quizPinyin: QuizPinyin?) = executor.execute {
        mQuizDao.insert(quizPinyin)
    }

    fun resetAsked(quizId: Long?) {
        if (quizId == null) {
            val message = "Null quizId in resetAsked."
            Log.e(TAG, message)
            throw IllegalArgumentException(message)
        }
        executor.execute {
            mQuizDao.resetAsked(quizId)
        }
    }

    fun updateQuizPinyin(quizPinyin: QuizPinyin?) {
        if (quizPinyin == null) {
            val message = "Null quizPinyin in updateQuizPinyin."
            Log.e(TAG, message)
            throw IllegalArgumentException(message)
        }
        executor.execute {
            mQuizDao.updateQuizPinyin(
                quizPinyin.quizId,
                quizPinyin.pinyinString,
                quizPinyin.isAsked
            )
        }
    }

    fun deleteQuiz(quizId: Long) = mQuizDao.deleteQuiz(quizId)

    companion object {
        private const val TAG = "QuizRepository"
    }
}