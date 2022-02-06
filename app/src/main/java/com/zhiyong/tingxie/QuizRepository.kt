package com.zhiyong.tingxie

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.db.*
import com.zhiyong.tingxie.network.*
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual
import com.zhiyong.tingxie.ui.friend.individual.request.others.TingXieOtherIndividualRequest
import com.zhiyong.tingxie.ui.friend.individual.request.yours.TingXieYourIndividualRequest
import com.zhiyong.tingxie.ui.main.QuizItem
import com.zhiyong.tingxie.ui.hsk.words.HskWordsAdapter
import com.zhiyong.tingxie.ui.share.EnumQuizRole
import com.zhiyong.tingxie.ui.share.TingXieShareIndividual
import com.zhiyong.tingxie.ui.word.WordItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future


/* A Repository is a class that abstracts access to multiple data sources.
A Repository manages query threads and allows you to use multiple backends.
In the most common example, the Repository implements the logic for deciding whether to fetch data
from a network or use results cached in the local database. */
class QuizRepository(val context: Context) {
  // todo: val database: PinyinRoomDatabase instead of val context: Context.
  private val mQuizDao: QuizDao = getDatabase(context).pinyinDao

  private val executor: ExecutorService = Executors.newSingleThreadExecutor()

  private lateinit var email: String

  init {
    val email = FirebaseAuth.getInstance().currentUser?.email
    if (email == null) {
      // todo Log Crashlytics
    } else {
      this.email = email
    }
  }

  val quizzes: LiveData<List<QuizItem>> = Transformations.map(mQuizDao.allQuizItems) {
    it.asDomainModel()
  }

  private fun syncLocalDeletedRows() {

  }

  private fun syncLocalInsertedRows() {

  }

  private fun syncLocalUpdatedRows() {

  }

  private fun syncRemoteDeletedRows() {

  }

  private fun syncRemoteInsertedRows() {

  }

  private fun syncRemoteUpdatedRows() {

  }

  suspend fun refreshQuizzes(quizItems: List<QuizItem>) {
    withContext(Dispatchers.IO) {
      // Send local quizIds to server.
      // Server responds with new remote quizzes and missing remote quizIds.
      val refreshQuizzesResponse = TingXieNetwork.tingxie.getNewQuizzes(
          email, NetworkQuizIdContainer(quizItems.map { it.id })
      )
      // Insert these new quizzes into local DB.
      mQuizDao.insertAll(refreshQuizzesResponse.newQuizzesRemote.map {
        Quiz(it.id, it.date, it.title, it.total_words, it.not_learned, it.round)
      })
      // Send missing quizzes to server.
      TingXieNetwork.tingxie.putQuizzes(
          email,
          NetworkQuizContainer(
              quizItems.filter { refreshQuizzesResponse.missingQuizIds.contains(it.id) }
                  .map { NetworkQuiz(
                      it.id, it.date, it.title, it.totalWords, it.notLearned, it.round
                  ) }
          )
      )

      // Insert quizzes in local DB not in remote DB.
      // status flag. inserted (0), updated (1), deleted (-1).
      // sync flag. synced (1), unsynced (0).
      // Local rows marked as -1 should be deleted
    }
  }

//  suspend fun refreshWordItemsOfQuiz(wordItemsOfQuiz: List<WordItem>) {
//    withContext(Dispatchers.IO) {
//      val refreshWordItemsResponse = TingXieNetwork.tingxie.getWordItemsOfQuiz(
//          email, NetworkQuizIdContainer(wordItemsOfQuiz.map { it.id })
//      )
//    }
//  }

  suspend fun getFriends(): List<TingXieIndividual> {
//        return TingXieNetwork.tingxie.getFriends().asDomainModel()
    try {
      TingXieNetwork.tingxie.getFriends(email).asDomainModel()
    } catch (e: Exception) {

    }
    return arrayListOf(TingXieIndividual("test0@email.com", "first0", "last0"), TingXieIndividual("test1@email.com", "first1", "last1"))
  }

  suspend fun addFriend(individual: TingXieIndividual) {
    TingXieNetwork.tingxie.postFriend(
        email,
        NetworkIndividual(individual.email, individual.firstName, individual.lastName)
    )
  }

  suspend fun deleteFriend(email: String) {
    TingXieNetwork.tingxie.deleteFriend(this.email, email)
  }

  suspend fun getYourIndividualRequests(): List<TingXieYourIndividualRequest> {
    try {
      TingXieNetwork.tingxie.getYourIndividualRequests(email)
    } catch (e: Exception) {

    }
    return arrayListOf(
        TingXieYourIndividualRequest("e0@email.com" , 20001010),
        TingXieYourIndividualRequest("e1@email.com", 20101020)
    )
  }

  suspend fun addYourIndividualRequest(request: TingXieYourIndividualRequest) =
      TingXieNetwork.tingxie.postYourIndividualRequest(
          email, NetworkYourIndividualRequest(request.email, request.date)
      )

  suspend fun deleteYourIndividualRequest(email: String) =
      TingXieNetwork.tingxie.deleteYourIndividualRequest(this.email, email)

  suspend fun getOtherIndividualRequests(): List<TingXieOtherIndividualRequest> {
    try {
      TingXieNetwork.tingxie.getOtherIndividualRequests(email)
    } catch (e: Exception) {

    }
    return arrayListOf(
        TingXieOtherIndividualRequest("e0@email.com" , "f0", "l0", 20001010),
        TingXieOtherIndividualRequest("e1@email.com", "f1", "l1", 20101020)
    )
  }

  suspend fun acceptOtherIndividualRequest(email: String) =
      TingXieNetwork.tingxie.putOtherIndividualRequest(email, this.email, true)

  suspend fun rejectOtherIndividualRequest(email: String) =
      TingXieNetwork.tingxie.putOtherIndividualRequest(email, this.email, false)

  suspend fun getShares(quizId: Long): List<TingXieShareIndividual> {
    try {
//            return TingXieNetwork.tingxie.getShares(quizId).asDomainModel()
      TingXieNetwork.tingxie.getShares(email, quizId).asDomainModel()
    } catch (e: Exception) {

    }
    return arrayListOf(
        TingXieShareIndividual("yongish@gmail.com", "firstZ", "lastZ", true, EnumQuizRole.EDITOR),
        TingXieShareIndividual("test0email.com", "first0", "last0", false, EnumQuizRole.EDITOR),
        TingXieShareIndividual("test1email.com", "first1", "last1", false, EnumQuizRole.VIEWER),
        TingXieShareIndividual("test2email.com", "first2", "last2", true, EnumQuizRole.VIEWER),
    )
//    return arrayListOf()
  }

  suspend fun addShare(quizId: Long, shareIndividual: TingXieShareIndividual) {
    TingXieNetwork.tingxie.postShare(email, quizId, NetworkShare(
        shareIndividual.email, shareIndividual.firstName, shareIndividual.lastName, true, shareIndividual.role
    ))
  }

  suspend fun shareAll(quizId: Long, shared: Boolean): List<TingXieShareIndividual> {
    TingXieNetwork.tingxie.putShareAll(email, quizId, shared)
    return arrayListOf()
  }

  suspend fun deleteShare(quizId: Long, email: String) {
    TingXieNetwork.tingxie.deleteShare(this.email, quizId, email)
  }

  val allQuizPinyins: LiveData<List<QuizPinyin>> = mQuizDao.allQuizPinyins
  val allQuestions: LiveData<List<Question>> = mQuizDao.allQuestions

  fun getQuizItem(quizId: Long): LiveData<QuizItem> {
    return mQuizDao.getQuizItem(quizId)
  }

  fun getWordItemsOfQuiz(quizId: Long): LiveData<List<WordItem>> {
    return mQuizDao.getWordItemsOfQuiz(quizId)
  }

  fun getRemainingQuestions(quizId: Long): LiveData<List<WordItem>> {
    return mQuizDao.getRemainingQuestions(quizId)
  }

  private var hskWordMap: MutableMap<Int, List<HskWordsAdapter.HskWord>> = mutableMapOf()

  init {
    for (level in 1..6) {
      val wordArray = JSONArray(context.assets
          .open("hsk-vocab-json/hsk-level-$level.json").bufferedReader().use {
            it.readText()
          }
      )
      val hskWords: MutableList<HskWordsAdapter.HskWord> = mutableListOf()
      for (i in 0 until wordArray.length()) {
        val word = wordArray.getJSONObject(i)
        val id = word.getInt("id")
        val hanzi = word.getString("hanzi")
        val pinyin = word.getString("pinyin")
        hskWords.add(HskWordsAdapter.HskWord(id, hanzi, pinyin))
      }
      hskWordMap[level] = hskWords
    }
  }

  fun getHsk(level: Int): List<HskWordsAdapter.HskWord> {
    return hskWordMap[level]!!
  }

  fun getUnaskedHskWords(level: Int): List<HskWordsAdapter.HskWord> {
    val wordList = getHsk(level)
    val unaskedIds = HashSet(wordList.map { word -> word.index })
    // todo: Deduplicate this shared preferences code.
    val sharedPreferences = context.getSharedPreferences("hsk", Context.MODE_PRIVATE)
    val askedIds = HashSet(sharedPreferences.getStringSet("askedHskIds", setOf())!!.map { id -> id.toInt() })
    unaskedIds.removeAll(askedIds)
    return wordList.filter { it.index in unaskedIds }
  }

  fun getHanzis(level: Int, pinyin: String): List<String> {
    val wordList = getHsk(level)
    return wordList.filter { it.pinyin == pinyin }.map { it.hanzi }
  }

  fun resetAsked(level: Int): Boolean {
    val wordList = getHsk(level)
    val ids = HashSet(wordList.map { word -> word.index })
    val sharedPreferences = context.getSharedPreferences("hsk", Context.MODE_PRIVATE)
    val askedIds = HashSet(sharedPreferences.getStringSet("askedHskIds", setOf())!!.filter { it.toInt() !in ids })
    val editor = sharedPreferences.edit()
    editor.putStringSet("askedHskIds", askedIds)
    return editor.commit()
  }

  fun setAsked(index: Int) {
    val sharedPreferences = context.getSharedPreferences("hsk", Context.MODE_PRIVATE)
    val askedIds = HashSet(sharedPreferences.getStringSet("askedHskIds", setOf())!!.map { id -> id.toInt() })
    askedIds.add(index)
    val editor = sharedPreferences.edit()
    editor.putStringSet("askedHskIds", HashSet(askedIds.map { id -> id.toString() }))
    editor.apply()
  }

  fun insertQuiz(quiz: Quiz?) {
    mQuizDao.insert(quiz)
    // todo: Current user should be an editor of this newly-created quiz.
    val email = FirebaseAuth.getInstance().currentUser?.email
    if (email == null || quiz == null) {
      throw IllegalStateException("Quiz or email is null.")
    } else {
      mQuizDao.insert(QuizRole(quiz.id, email, EnumQuizRole.EDITOR))
    }
  }

  fun insertQuizFuture(quiz: Quiz?): Future<Long> = executor.submit(Callable {
    mQuizDao.insert(quiz)
  })

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