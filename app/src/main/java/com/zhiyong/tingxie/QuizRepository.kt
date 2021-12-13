package com.zhiyong.tingxie

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.db.*
import com.zhiyong.tingxie.network.*
import com.zhiyong.tingxie.ui.friend.group.name.TingXieFriendGroup
import com.zhiyong.tingxie.ui.friend.group.member.TingXieGroupMember
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual
import com.zhiyong.tingxie.ui.main.QuizItem
import com.zhiyong.tingxie.ui.hsk.words.HskWordsAdapter
import com.zhiyong.tingxie.ui.share.EnumQuizRole
import com.zhiyong.tingxie.ui.share.TingXieShareIndividual
import com.zhiyong.tingxie.ui.share.TingXieShareGroup
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

  suspend fun refreshQuizzes() {
    withContext(Dispatchers.IO) {
      // Insert quizzes in local DB not in remote DB.

      // status flag. inserted (0), updated (1), deleted (-1).
      // sync flag. synced (1), unsynced (0).

      // Local rows marked as -1 should be deleted


      // Insert quizzes in remote DB not in local DB.
      val quizzes = TingXieNetwork.tingxie.getQuizzes(email)


      mQuizDao.insertAll(quizzes.asDatabaseModel())
    }
  }

  suspend fun getFriendGroups(): List<TingXieFriendGroup> {
    try {
        TingXieNetwork.tingxie.getGroups(email).asDomainModel()
    } catch (e: Exception) {
      // todo: Log to Crashlytics.
    }
    return arrayListOf(
        TingXieFriendGroup("group0", arrayListOf(
            TingXieGroupMember("g0i0@email.com", EnumQuizRole.EDITOR, "g0f0", "g0l0"),
            TingXieGroupMember("g0i1@email.com", EnumQuizRole.EDITOR, "g0f1", "g0l1"),
            TingXieGroupMember("g0i2@email.com", EnumQuizRole.EDITOR, "g0f2", "g0l2"),
        )),
        TingXieFriendGroup("group1", arrayListOf(
            TingXieGroupMember("g1i0@email.com", EnumQuizRole.VIEWER, "g1f0", "g1l0"),
            TingXieGroupMember("g1i1@email.com", EnumQuizRole.EDITOR, "g1f1", "g1l1"),
        )),
    )
  }

  suspend fun addGroup(group: TingXieFriendGroup) {
    TingXieNetwork.tingxie.postGroup(email, NetworkGroup(
        group.name,
        group.members.map { NetworkGroupMember(it.email, it.role, it.firstName, it.lastName) }
    ))
  }

  suspend fun deleteGroup(email: String, name: String) {
    TingXieNetwork.tingxie.deleteGroup(name, this.email, email)
  }

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

  suspend fun getYourIndividualRequests(): Map<String, Int> {
    TingXieNetwork.tingxie.getYourIndividualRequests(email)
    return mapOf("e0@email.com" to 20001010, "e1@email.com" to 20101020)
  }

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

  suspend fun deleteShare(quizId: Long, email: String) {
    TingXieNetwork.tingxie.deleteShare(this.email, quizId, email)
  }

  suspend fun getShareGroups(quizId: Long): List<TingXieShareGroup> {
    try {
//      return TingXieNetwork.tingxie.getShareGroups(email, quizId)
      TingXieNetwork.tingxie.getShareGroups(email, quizId)
    } catch (e: Exception) {

    }
    return arrayListOf(
        TingXieShareGroup("group0", true, arrayListOf(
            TingXieGroupMember("g0i0@email.com", EnumQuizRole.EDITOR, "g0f0", "g0l0"),
            TingXieGroupMember("g0i1@email.com", EnumQuizRole.EDITOR, "g0f1", "g0l1"),
            TingXieGroupMember("g0i2@email.com", EnumQuizRole.EDITOR, "g0f2", "g0l2"),
        )),
        TingXieShareGroup("group1", false, arrayListOf(
            TingXieGroupMember("g1i0@email.com", EnumQuizRole.VIEWER, "g1f0", "g1l0"),
            TingXieGroupMember("g1i1@email.com", EnumQuizRole.EDITOR, "g1f1", "g1l1"),
        )),
    )
  }

  suspend fun addShareGroup(quizId: Long, name: String) {
    TingXieNetwork.tingxie.postShareGroup(name, email, quizId)
  }

  suspend fun deleteShareGroup(quizId: Long, name: String) {
    TingXieNetwork.tingxie.deleteShareGroup(name, email, quizId)
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