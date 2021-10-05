package com.zhiyong.tingxie.ui.hsk.words

data class HskQuestionItem(
  val id: Int,
  val asked: Boolean,
  val level: Int,
  val hanzi: String,
  val pinyin: String
)
