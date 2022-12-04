package com.zhiyong.tingxie.ui.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizItem(val id: Long,
                    var date: Int,
                    var title: String,
                    var numWords: Int,
                    var numNotCorrect: Int,
                    var round: Int,
) : Parcelable
