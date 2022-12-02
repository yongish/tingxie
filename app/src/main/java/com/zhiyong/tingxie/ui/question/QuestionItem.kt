package com.zhiyong.tingxie.ui.question;

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionItem(val characters: String, val pinyin: String) : Parcelable
