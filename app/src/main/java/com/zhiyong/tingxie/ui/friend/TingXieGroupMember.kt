package com.zhiyong.tingxie.ui.friend

import android.os.Parcelable
import com.zhiyong.tingxie.ui.share.EnumQuizRole
import kotlinx.android.parcel.Parcelize

@Parcelize
class TingXieGroupMember(
    val email: String, val role: EnumQuizRole, val firstName: String, val lastName: String,
) : Parcelable
