package com.zhiyong.tingxie.ui

import android.os.Parcelable
import com.zhiyong.tingxie.ui.share.EnumQuizRole
import kotlinx.parcelize.Parcelize

const val EXTRA_ROLE = "com.zhiyong.tingxie.ui.group.extra.EXTRA_ROLE"

@Parcelize
data class UserRole(val id: Long, val role: EnumQuizRole): Parcelable
