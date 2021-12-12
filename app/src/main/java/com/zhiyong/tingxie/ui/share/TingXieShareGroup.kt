package com.zhiyong.tingxie.ui.share

import android.os.Parcelable
import com.zhiyong.tingxie.ui.friend.TingXieGroupMember
import kotlinx.parcelize.Parcelize

@Parcelize
class TingXieShareGroup(
    val name: String, val isShared: Boolean, val members: List<TingXieGroupMember>
    ) : Parcelable
