package com.zhiyong.tingxie.db

import androidx.annotation.NonNull
import androidx.room.*

@Entity(indices = [Index("id")])
class Quiz {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  @NonNull
  var id: Long = 0
  @NonNull
  var date: Int
  @NonNull
  var title: String

  @ColumnInfo(name = "total_words")
  @NonNull
  var totalWords // todo: need to update these values.
          : Int

  @ColumnInfo(name = "not_learned")
  @NonNull
  var notLearned: Int
  @NonNull
  var round: Int

  constructor(date: Int) {
    this.date = date
    title = "No title"
    totalWords = 0
    notLearned = 0
    round = 1
  }

  @Ignore
  constructor(
    id: Long, date: Int, title: String,
    totalWords: Int, notLearned: Int, round: Int
  ) {
    this.id = id
    this.date = date
    this.title = title
    this.totalWords = totalWords
    this.notLearned = notLearned
    this.round = round
  }
}