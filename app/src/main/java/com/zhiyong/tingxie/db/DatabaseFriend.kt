package com.zhiyong.tingxie.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhiyong.tingxie.ui.friends.TingXieFriend

@Entity
data class DatabaseFriend constructor(@PrimaryKey var email: String)

fun List<DatabaseFriend>.asDomainModel(): List<TingXieFriend> {
  return map {
    TingXieFriend(email = it.email)
  }
}
