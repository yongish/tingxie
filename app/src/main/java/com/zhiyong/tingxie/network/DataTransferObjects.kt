package com.zhiyong.tingxie.network

import com.squareup.moshi.JsonClass
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.ui.friend.group.name.TingXieGroup
import com.zhiyong.tingxie.ui.friend.group.member.TingXieGroupMember
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual
import com.zhiyong.tingxie.ui.share.EnumQuizRole
import com.zhiyong.tingxie.ui.share.TingXieShareIndividual

@JsonClass(generateAdapter = true)
data class NetworkQuizContainer(val quizzes: List<NetworkQuiz>)

@JsonClass(generateAdapter = true)
data class NetworkQuiz(val id: Long,
                       val date: Int,
                       val title: String,
                       val total_words: Int,
                       val not_learned: Int,
                       val round: Int)

fun NetworkQuizContainer.asDatabaseModel(): List<Quiz> = quizzes.map {
  Quiz(it.id, it.date, it.title, it.total_words, it.not_learned, it.round)
}

@JsonClass(generateAdapter = true)
data class NetworkIndividualContainer(val individuals: List<NetworkIndividual>)

fun NetworkIndividualContainer.asDomainModel(): List<TingXieIndividual> =
    individuals.map { TingXieIndividual(it.email, it.firstName, it.lastName) }

@JsonClass(generateAdapter = true)
data class NetworkIndividual(val email: String, val firstName: String, val lastName: String)

//fun NetworkFriendContainer.asDatabaseModel(): List<DatabaseFriend> {
//  return friends.map {
//    DatabaseFriend(it.email)
//  }
//}

@JsonClass(generateAdapter = true)
data class NetworkGroupContainer(val groups: List<NetworkGroup>)

@JsonClass(generateAdapter = true)
data class NetworkGroupMember(val email: String,
                              val role: EnumQuizRole,
                              val firstName: String,
                              val lastName: String)

fun NetworkGroupContainer.asDomainModel(): List<TingXieGroup> = groups.map {
  TingXieGroup(it.name, it.individuals.map {
    it1 -> TingXieGroupMember(it1.email, it1.role, it1.firstName, it1.lastName)
  } )
}

@JsonClass(generateAdapter = true)
data class NetworkGroup(val name: String, val individuals: List<NetworkGroupMember>)

@JsonClass(generateAdapter = true)
data class NetworkShareContainer(val shares: List<NetworkShare>)

@JsonClass(generateAdapter = true)
data class NetworkShare(val email: String,
                        val firstName: String,
                        val lastName: String,
                        val isShared: Boolean,
                        val role: EnumQuizRole)

fun NetworkShareContainer.asDomainModel(): List<TingXieShareIndividual> = shares.map {
  TingXieShareIndividual(it.email, it.firstName, it.lastName, it.isShared, it.role)
}

@JsonClass(generateAdapter = true)
data class NetworkShareGroupContainer(val shareGroups: List<NetworkShareGroup>)

@JsonClass(generateAdapter = true)
data class NetworkShareGroup(val name: String,
                             val isShared: Boolean,
                             val individuals: List<NetworkGroupMember>)

//fun NetworkShareGroupContainer.asDomainModel(): Lddist<TingXie>
