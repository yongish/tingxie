package com.zhiyong.tingxie.db

import androidx.annotation.NonNull
import androidx.room.*
import com.zhiyong.tingxie.ui.main.QuizItem

@Entity(indices = [Index("id")])
data class Quiz (
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  @NonNull
  var id: Long,
  @NonNull val date: Int,
  @NonNull val title: String = "No title",
  @ColumnInfo(name = "total_words") @NonNull val totalWords: Int = 0,
  @ColumnInfo(name = "not_learned") @NonNull val notLearned: Int = 0,
  @NonNull val round: Int = 1
  ) {
  @Ignore
  constructor(date: Int) : this(0, date)
}

fun List<Quiz>.asDomainModel(): List<QuizItem> {
  return map {
    QuizItem(
      id = it.id,
      date = it.date,
      title = it.title,
      totalWords = it.totalWords,
      notLearned = it.notLearned,
      round = it.round
    )
  }
}