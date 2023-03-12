package com.zhiyong.tingxie.ui.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class QuizStatus : Parcelable {
  NOT_DELETED, CLIENT_DELETED, SERVER_DELETED
}
