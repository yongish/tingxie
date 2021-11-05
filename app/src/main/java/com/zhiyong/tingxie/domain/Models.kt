package com.zhiyong.tingxie.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TingXieQuiz(val id: Long,
                       val date: Int,
                       val title: String,
                       val totalWords: Int,
                       val notLearned: Int,
                       val round: Int) : Parcelable
