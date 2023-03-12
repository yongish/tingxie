package com.zhiyong.tingxie.ui.main

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MigrateLocal(
  val email: String,
  val name: String,
  val quizzes: List<MigrateQuiz>
)

@JsonClass(generateAdapter = true)
data class MigrateQuiz(
  val date: Int,
  val title: String,
  val totalWords: Int,
  val notLearned: Int,
  val round: Int,
  val migrateWords: List<MigrateWord>
)

@JsonClass(generateAdapter = true)
data class MigrateWord(val pinyin: String, val word: String, val asked: Boolean)
