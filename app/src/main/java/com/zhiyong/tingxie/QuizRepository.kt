package com.zhiyong.tingxie

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import com.zhiyong.tingxie.db.Question
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.db.Word
import com.zhiyong.tingxie.ui.main.QuizItem
import com.zhiyong.tingxie.ui.word.WordItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/* A Repository is a class that abstracts access to multiple data sources.
A Repository manages query threads and allows you to use multiple backends.
In the most common example, the Repository implements the logic for deciding whether to fetch data
from a network or use results cached in the local database. */
class QuizRepository(database: PinyinRoomDatabase, quizId: Long) {
    private val mQuizDao: QuizDao = database.pinyinDao
    val allQuizItems: LiveData<List<QuizItem>>
    val quizItem: LiveData<QuizItem>
    val wordItemsOfQuiz: LiveData<List<WordItem>>
    val allQuizPinyins: LiveData<List<QuizPinyin>>
    val allQuestions: LiveData<List<Question>>
    val remainingQuestionsOfQuiz: LiveData<List<WordItem>>

    fun insertQuiz(quiz: Quiz?) = mQuizDao.insert(quiz)

    fun deleteQuizPinyins(quizId: Long) = mQuizDao.deleteQuizPinyins(quizId)

    fun updateQuiz(quiz: Quiz?) {
        updateQuizAsyncTask(mQuizDao).execute(quiz)
    }

    private class updateQuizAsyncTask internal constructor(private val mAsyncTaskDao: QuizDao) : AsyncTask<Quiz?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Quiz?): Void? {
            mAsyncTaskDao.update(p0[0])
            return null
        }
    }

    fun insertQuestion(question: Question?) {
        insertQuestionAsyncTask(mQuizDao).execute(question)
    }

    private class insertQuestionAsyncTask internal constructor(private val mAsyncTaskDao: QuizDao) : AsyncTask<Question?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Question?): Void? {
            mAsyncTaskDao.insert(p0[0])
            return null
        }
    }

    fun updateQuestions(quizId: Long?) {
        updateQuestionsAsyncTask(mQuizDao).execute(quizId)
    }

    private class updateQuestionsAsyncTask internal constructor(private val mAsyncTaskDao: QuizDao) : AsyncTask<Long?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Long?): Void? {
            p0[0]?.let { mAsyncTaskDao.updateQuestions(it, System.currentTimeMillis()) }
            return null
        }
    }

    fun addWord(quizId: Long, wordString: String?, pinyinString: String?) {
        val wordItem = WordItem(quizId, wordString, pinyinString, false)
        addWordAsyncTask(mQuizDao).execute(wordItem)
    }

    private class addWordAsyncTask internal constructor(private val mAsyncTaskDao: QuizDao) : AsyncTask<WordItem?, Void?, Void?>() {
        override fun doInBackground(vararg p0: WordItem?): Void? {
            val pinyinString = p0[0]?.pinyinString
            val wordString = p0[0]?.wordString
            // todo: Think Word table is not used anymore can be deleted.
            mAsyncTaskDao.insert(Word(wordString!!, pinyinString!!))
            mAsyncTaskDao.insert(p0[0]?.quizId?.let { QuizPinyin(it, pinyinString, wordString, false) })
            return null
        }
    }

    // Only delete QuizPinyin object.
    fun deleteWord(quizPinyin: QuizPinyin?) {
        deleteWordAsyncTask(mQuizDao).execute(quizPinyin)
    }

    private class deleteWordAsyncTask internal constructor(private val mAsyncTaskDao: QuizDao) : AsyncTask<QuizPinyin?, Void?, Void?>() {
        override fun doInBackground(vararg p0: QuizPinyin?): Void? {
            // Word and Pinyin are immutable. Only QuizPinyin object is deleted.
            p0[0]?.quizId?.let { mAsyncTaskDao.deleteQuizPinyin(it, p0[0]?.pinyinString) }
            return null
        }

    }

    // Undo a just deleted word.
    fun insertQuizPinyin(quizPinyin: QuizPinyin?) {
        insertQuizPinyinAsyncTask(mQuizDao).execute(quizPinyin)
    }

    private class insertQuizPinyinAsyncTask internal constructor(private val mAsyncTaskDao: QuizDao) : AsyncTask<QuizPinyin?, Void?, Void?>() {
        override fun doInBackground(vararg p0: QuizPinyin?): Void? {
            mAsyncTaskDao.insert(p0[0])
            return null
        }
    }

    fun resetAsked(quizId: Long?) {
        resetAskedAsyncTask(mQuizDao).execute(quizId)
    }

    private class resetAskedAsyncTask internal constructor(private val mAsyncTaskDao: QuizDao) : AsyncTask<Long?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Long?): Void? {
            mAsyncTaskDao.resetAsked(p0[0]!!)
            return null
        }
    }

    fun updateQuizPinyin(quizPinyin: QuizPinyin?) {
        updateQuizPinyinAsyncTask(mQuizDao).execute(quizPinyin)
    }

    private class updateQuizPinyinAsyncTask internal constructor(private val mAsyncTaskDao: QuizDao) : AsyncTask<QuizPinyin?, Void?, Void?>() {
        override fun doInBackground(vararg p0: QuizPinyin?): Void? {
            mAsyncTaskDao.updateQuizPinyin(p0[0]!!.quizId, p0[0]!!.pinyinString, p0[0]!!.isAsked)
            return null
        }
    }

    fun deleteQuiz(quizId: Long) {
        deleteQuizItemAsyncTask(mQuizDao).execute(quizId)
    }

    private class deleteQuizItemAsyncTask internal constructor(private val mAsyncTaskDao: QuizDao) : AsyncTask<Long?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Long?): Void? {
            p0[0]?.let { mAsyncTaskDao.deleteQuiz(it) }
            return null
        }
    }

    companion object {
        private const val TAG = "QuizRepository"
    }

    init {
//        val db: PinyinRoomDatabase = getDatabase(application)
        Log.d(TAG, "QuizRepository: ")
        allQuizItems = mQuizDao.allQuizItems
        quizItem = mQuizDao.getQuizItem(quizId)
        wordItemsOfQuiz = mQuizDao.getWordItemsOfQuiz(quizId)
        allQuizPinyins = mQuizDao.allQuizPinyins
        allQuestions = mQuizDao.allQuestions
        remainingQuestionsOfQuiz = mQuizDao.getRemainingQuestions(quizId)
    }
}