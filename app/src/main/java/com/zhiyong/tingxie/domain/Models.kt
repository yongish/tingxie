package com.zhiyong.tingxie.domain

data class TingXieQuiz(val date: Int,
                       val title: String,
                       val totalWords: Int,
                       val notLearned: Int,
                       val round: Int)
