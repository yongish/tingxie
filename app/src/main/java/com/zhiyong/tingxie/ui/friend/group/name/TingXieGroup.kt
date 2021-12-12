package com.zhiyong.tingxie.ui.friend.group.name

import android.os.Parcelable
import com.zhiyong.tingxie.ui.friend.group.member.TingXieGroupMember
import kotlinx.android.parcel.Parcelize

@Parcelize
class TingXieGroup(
    val name: String, val members: List<TingXieGroupMember> = arrayListOf()
) : Parcelable
