package com.zhiyong.tingxie.ui.friend

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zhiyong.tingxie.ui.friend.group.name.FriendGroupNameFragment
import com.zhiyong.tingxie.ui.friend.group.requests.others.OtherGroupRequestFragment
import com.zhiyong.tingxie.ui.friend.group.requests.yours.YourGroupRequestFragment
import com.zhiyong.tingxie.ui.friend.individual.FriendIndividualFragment
import com.zhiyong.tingxie.ui.friend.individual.request.others.OtherRequestFragment
import com.zhiyong.tingxie.ui.friend.individual.request.yours.YourRequestFragment

class FriendAdapter(activity: AppCompatActivity, private val itemsCount: Int)
  : FragmentStateAdapter(activity) {
  override fun getItemCount(): Int = itemsCount

  override fun createFragment(position: Int): Fragment {
    if (position == 0) {
      return FriendIndividualFragment.newInstance()
    }
    if (position == 1) {
      return FriendGroupNameFragment.newInstance()
    }
    if (position == 2) {
      return YourRequestFragment.newInstance()
    }
    if (position == 3) {
      return OtherRequestFragment.newInstance()
    }
    if (position == 4) {
      return YourGroupRequestFragment.newInstance()
    }
    return OtherGroupRequestFragment.newInstance()
  }
}
