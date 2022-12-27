package com.zhiyong.tingxie

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.db.*
import com.zhiyong.tingxie.network.*
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual
import com.zhiyong.tingxie.ui.hsk.words.HskWordsAdapter
import com.zhiyong.tingxie.ui.word.WordItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.HashSet


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
    // 12/8/22. Should use the user details in the service layer rather than the
    // repository layer, but keeping this here because there is so much code that uses it
    // already.
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

  suspend fun putToken(uid: String, email: String, token: String) {
    withContext(Dispatchers.IO) {
      TingXieNetwork.tingxie.putToken(
        NetworkToken(
          uid,
          email,
          token
        )
      )
    }
  }

  suspend fun getQuizzes(): List<NetworkQuiz> = TingXieNetwork.tingxie.getQuizzes(email)

  suspend fun getWordItemsOfQuiz(quizId: Long): List<WordItem> =
    TingXieNetwork.tingxie.getWordItemsOfQuiz(quizId)
      .map { it.asDomainModel(quizId) }

  suspend fun notCorrectWordsRandomOrder(quizId: Long): List<NetworkWordItem> =
    TingXieNetwork.tingxie.notCorrectWordsRandomOrder(quizId, email)

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

  suspend fun createQuiz(title: String, date: Int): Long {
    return TingXieNetwork.tingxie.postQuiz(NetworkCreateQuiz(title, date, name, email))
  }

  suspend fun updateQuiz(quiz: NetworkQuiz): Int {
    // todo: REPAIR THIS.
    return TingXieNetwork.tingxie.putQuiz(quiz)
//      NetworkQuiz(
//        quiz.id,
//        quiz.title,
//        quiz.date,
//        email,
//        "STUB. REPLACE THIS",
//        quiz.numWords,
//        quiz.numNotCorrect,
//        quiz.round
//      )
//    )
  }

  suspend fun addWord(quizId: Long, wordString: String?, pinyinString: String?): Long {
    if (wordString == null || pinyinString == null) {
      throw IllegalArgumentException("Null wordString or pinyinString.")
    }
    return TingXieNetwork.tingxie.postWord(
      quizId,
      NetworkCreateWord(wordString, pinyinString)
    )
  }

  suspend fun deleteWord(id: Long) = TingXieNetwork.tingxie.deleteWord(id)

  // Undo a just deleted word.
  fun insertQuizPinyin(quizPinyin: QuizPinyin?) = executor.execute {
    mQuizDao.insert(quizPinyin)
  }

  suspend fun deleteQuiz(quizId: Long) =
    TingXieNetwork.tingxie.deleteQuiz(quizId, name, email, email)

  suspend fun upsertCorrectRecord(wordId: Long) =
    TingXieNetwork.tingxie.upsertCorrectRecord(NetworkCorrectRecord(wordId, email))

  suspend fun getGroups(): MutableList<NetworkGroup> =
    TingXieNetwork.tingxie.getGroups(email)

  suspend fun createGroup(groupName: String, members: List<NetworkGroupMember>): String =
    TingXieNetwork.tingxie.createGroup(
      NetworkCreateGroup(groupName, name, email, members)
    )

  suspend fun deleteGroup(
    groupId: Long,
    requesterName: String,
    requesterEmail: String
  ): Int = TingXieNetwork.tingxie.deleteGroup(groupId, requesterName, requesterEmail)

  suspend fun getGroupMembers(groupId: Long) =
    TingXieNetwork.tingxie.getGroupMembers(groupId)

  suspend fun addGroupMemberOrReturnNoUser(
    groupId: Long,
    email: String,
    role: String
  ): NetworkGroupMember =
    TingXieNetwork.tingxie.addGroupMemberOrReturnNoUser(groupId, email, role)

  suspend fun changeRole(groupId: Long, email: String, role: String): Int =
    TingXieNetwork.tingxie.changeRole(groupId, email, role)

  suspend fun deleteGroupMember(
    groupId: Long,
    requesterName: String,
    requesterEmail: String,
    email: String
  ): Int = TingXieNetwork.tingxie.deleteGroupMember(
    groupId,
    email,
    requesterName,
    requesterEmail
  )

  suspend fun getUsersOfQuiz(quizId: Long): List<NetworkGroupMember> =
    TingXieNetwork.tingxie.getUsersOfQuiz(quizId)

  suspend fun addQuizMemberOrReturnNoUser(
    quizId: Long,
    addQuizUser: NetworkAddQuizUser
  ): NetworkGroupMember =
    TingXieNetwork.tingxie.addQuizMemberOrReturnNoUser(quizId, addQuizUser)

  suspend fun removeQuizMember(
    quizId: Long,
    requesterName: String,
    requesterEmail: String,
    email: String
  ): String = TingXieNetwork.tingxie.deleteQuiz(quizId, requesterName, requesterEmail, email)


  // Endpoints below are for Friends API, which I decided not to use.
  suspend fun checkUserExists(email: String): Boolean =
    TingXieNetwork.tingxie.checkUserExists(email).toBoolean()

  suspend fun getFriends(party: String, friendStatus: String): List<TingXieIndividual> =
    TingXieNetwork.tingxie.getFriends(email, party, friendStatus)
      .map { it.asDomainModel() }

  suspend fun addFriend(individual: TingXieIndividual) =
    TingXieNetwork.tingxie.postFriend(
      NetworkIndividual(
        email,
        individual.email,
        name,
        individual.name,
        individual.status
      )
    )

  suspend fun updateFriend(individual: TingXieIndividual) =
    TingXieNetwork.tingxie.putFriend(
      NetworkIndividual(
        individual.email,
        email,
        name,
        individual.name,
        individual.status
      )
    )

  suspend fun deleteFriend(email: String) =
    TingXieNetwork.tingxie.deleteFriend(this.email, email)

//  suspend fun acceptOtherIndividualRequest(email: String) =
//    TingXieNetwork.tingxie.putOtherIndividualRequest(email, this.email, true)
//
//  suspend fun rejectOtherIndividualRequest(email: String) =
//    TingXieNetwork.tingxie.putOtherIndividualRequest(email, this.email, false)

//  suspend fun getShares(quizId: Long): List<TingXieShareIndividual> =
//    TingXieNetwork.tingxie.getShares(email, quizId).map {
//      TingXieShareIndividual(it.email, it.name, it.isShared, it.role)
//    }
//    return arrayListOf(
//        TingXieShareIndividual("yongish@gmail.com", "firstZ", "lastZ", true, EnumQuizRole.EDITOR),
//        TingXieShareIndividual("test0email.com", "first0", "last0", false, EnumQuizRole.EDITOR),
//        TingXieShareIndividual("test1email.com", "first1", "last1", false, EnumQuizRole.VIEWER),
//        TingXieShareIndividual("test2email.com", "first2", "last2", true, EnumQuizRole.VIEWER),
//    )
//    return arrayListOf()

//  suspend fun addShare(quizId: Long, shareIndividual: TingXieShareIndividual) {
//    TingXieNetwork.tingxie.postShare(
//      email, quizId, NetworkShare(
//        shareIndividual.email, shareIndividual.name, true, shareIndividual.role
//      )
//    )
//  }

//  suspend fun shareAll(quizId: Long, shared: Boolean): List<TingXieShareIndividual> {
//    TingXieNetwork.tingxie.putShareAll(email, quizId, shared)
//    return arrayListOf()
//  }

  suspend fun deleteShare(quizId: Long, email: String) {
    TingXieNetwork.tingxie.deleteShare(this.email, quizId, email)
  }

  companion object {
    private const val TAG = "QuizRepository"
  }
}