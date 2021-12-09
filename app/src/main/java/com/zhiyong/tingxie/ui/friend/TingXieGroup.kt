package com.zhiyong.tingxie.ui.friend

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TingXieGroup(
    val name: String, val individuals: List<TingXieGroupMember> = arrayListOf()
) : Parcelable
