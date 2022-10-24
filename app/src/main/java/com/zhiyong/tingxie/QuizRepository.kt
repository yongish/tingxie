package com.zhiyong.tingxie

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.db.*
import com.zhiyong.tingxie.network.*
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual
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
  private lateinit var name: String

  init {
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email
    val name = user?.displayName
    if (email == null || name == null) {
      // todo Log Crashlytics
    } else {
      this.email = email
      this.name = name
    }
  }

  // stopped here. next step is to request from remote, not local. Then sync with local.
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

  suspend fun putToken(uid: String, email: String, token: String) {
    withContext(Dispatchers.IO) {
      TingXieNetwork.tingxie.putToken(NetworkToken(uid, email, token))
    }
  }

  suspend fun refreshQuizzes(quizItems: List<QuizItem>) {
    withContext(Dispatchers.IO) {
      // Send local quizIds to server.
      // Server responds with new remote quizzes and missing remote quizIds.
      val refreshQuizzesResponse = TingXieNetwork.tingxie.refreshQuizzes(
        email, quizItems.map { NetworkQuizDeleted(it.id, it.status == "CLIENT_DELETED") }
      )
      if (refreshQuizzesResponse.new_quizzes_remote.isNotEmpty()) {
        // Insert these new quizzes into local DB.
        mQuizDao.insertAll(refreshQuizzesResponse.new_quizzes_remote.map {
          Quiz(it.quiz_id, it.date, it.title, it.total_words, it.not_learned, it.round)
        })
      }
      if (refreshQuizzesResponse.deleted_quiz_ids.isNotEmpty()) {
        mQuizDao.setQuizDeleted(refreshQuizzesResponse.deleted_quiz_ids)
      }
      if (refreshQuizzesResponse.missing_quiz_ids.isNotEmpty()) {
        // Send missing quizzes to server.
        quizItems.filter { refreshQuizzesResponse.missing_quiz_ids.contains(it.id) }
          .forEach {
            TingXieNetwork.tingxie.postQuiz(
              NetworkCreateQuiz(
                it.id,
                it.title,
                it.totalWords,
                it.notLearned,
                it.round,
                it.date,
                email,
                name,
                "EDITOR"
              )
            )
          }
      }
    }
  }

  suspend fun refreshWordItemsOfQuiz(quizId: Long, wordItemsOfQuiz: List<WordItem>) {
    withContext(Dispatchers.IO) {
      val refreshWordItemsResponse = TingXieNetwork.tingxie.refreshWords(
        NetworkRefreshWords(email, quizId, wordItemsOfQuiz.map {
          NetworkWordItem(it.id, it.wordString, it.pinyinString, it.isAsked)
        })
      )
      if (refreshWordItemsResponse.isNotEmpty()) {
        mQuizDao.insertQuizPinyins(refreshWordItemsResponse.map {
          QuizPinyin(quizId, it.pinyin, it.characters, it.asked)
        })
      }
    }
  }

  suspend fun checkUserExists(email: String): Boolean =
    TingXieNetwork.tingxie.checkUserExists(email).toBoolean()

  suspend fun getFriends(party: String, friendStatus: String): List<TingXieIndividual> =
    TingXieNetwork.tingxie.getFriends(email, party, friendStatus)
      .map { it.asDomainModel() }

  suspend fun addFriend(individual: TingXieIndividual) =
    TingXieNetwork.tingxie.postFriend(
      NetworkIndividual(email, individual.email, name, individual.name, individual.status)
    )

  suspend fun updateFriend(individual: TingXieIndividual) =
    TingXieNetwork.tingxie.putFriend(
      NetworkIndividual(individual.email, email, name, individual.name, individual.status)
    )

  suspend fun deleteFriend(email: String) =
    TingXieNetwork.tingxie.deleteFriend(this.email, email)

//  suspend fun acceptOtherIndividualRequest(email: String) =
//    TingXieNetwork.tingxie.putOtherIndividualRequest(email, this.email, true)
//
//  suspend fun rejectOtherIndividualRequest(email: String) =
//    TingXieNetwork.tingxie.putOtherIndividualRequest(email, this.email, false)

  suspend fun getShares(quizId: Long): List<TingXieShareIndividual> =
    TingXieNetwork.tingxie.getShares(email, quizId).map {
      TingXieShareIndividual(it.email, it.name, it.isShared, it.role)
    }
//    return arrayListOf(
//        TingXieShareIndividual("yongish@gmail.com", "firstZ", "lastZ", true, EnumQuizRole.EDITOR),
//        TingXieShareIndividual("test0email.com", "first0", "last0", false, EnumQuizRole.EDITOR),
//        TingXieShareIndividual("test1email.com", "first1", "last1", false, EnumQuizRole.VIEWER),
//        TingXieShareIndividual("test2email.com", "first2", "last2", true, EnumQuizRole.VIEWER),
//    )
//    return arrayListOf()

  suspend fun addShare(quizId: Long, shareIndividual: TingXieShareIndividual) {
    TingXieNetwork.tingxie.postShare(
      email, quizId, NetworkShare(
        shareIndividual.email, shareIndividual.name, true, shareIndividual.role
      )
    )
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
    val askedIds = HashSet(
      sharedPreferences.getStringSet("askedHskIds", setOf())!!.map { id -> id.toInt() })
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
    val askedIds = HashSet(
      sharedPreferences.getStringSet("askedHskIds", setOf())!!
        .filter { it.toInt() !in ids })
    val editor = sharedPreferences.edit()
    editor.putStringSet("askedHskIds", askedIds)
    return editor.commit()
  }

  fun setAsked(index: Int) {
    val sharedPreferences = context.getSharedPreferences("hsk", Context.MODE_PRIVATE)
    val askedIds = HashSet(
      sharedPreferences.getStringSet("askedHskIds", setOf())!!.map { id -> id.toInt() })
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

  suspend fun addWord(quizId: Long, wordString: String?, pinyinString: String?) {
    // todo: Think Word table is not used anymore can be deleted.
    if (wordString == null || pinyinString == null) {
      throw IllegalArgumentException("Null wordString or pinyinString.")
    }
    executor.execute {
      mQuizDao.insert(Word(wordString, pinyinString))
      mQuizDao.insert(QuizPinyin(quizId, pinyinString, wordString, false))
    }
    TingXieNetwork.tingxie.postWord(
      NetworkCreateWord(email, quizId, wordString, pinyinString)
    )
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
        quizPinyin.asked
      )
    }
  }

  suspend fun deleteQuiz(quizId: Long) {
    mQuizDao.deleteQuiz(quizId)
    TingXieNetwork.tingxie.deleteQuiz(email, quizId)
  }

  companion object {
    private const val TAG = "QuizRepository"
  }
}