package com.zhiyong.tingxie.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hsk")
data class Hsk(
  @PrimaryKey val id: Int,
  val asked: Boolean,
  val level: Int,
  val hanzi: String,
  val pinyin: String
)
