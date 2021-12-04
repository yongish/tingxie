package com.zhiyong.tingxie.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhiyong.tingxie.ui.friend.TingXieIndividual

@Entity
data class DatabaseFriend constructor(
    @PrimaryKey var email: String,
    @NonNull val firstName: String,
    @NonNull val lastName: String,
)

fun List<DatabaseFriend>.asDomainModel(): List<TingXieIndividual> {
  return map {
    TingXieIndividual(it.email, it.firstName, it.lastName)
  }
}
