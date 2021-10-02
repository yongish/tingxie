package com.zhiyong.tingxie.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Hsk(
  @PrimaryKey val id: Int,
  val asked: Boolean,
  val hanzi: String,
  val pinyin: String
)
