package com.zhiyong.tingxie.ui.friend

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FriendAdapter(activity: AppCompatActivity, private val itemsCount: Int)
  : FragmentStateAdapter(activity) {
  override fun getItemCount(): Int = itemsCount

  override fun createFragment(position: Int): Fragment {
    if (position == 0) {
      return FriendIndividualFragment.newInstance()
    }
    return FriendGroupNameFragment.newInstance()
  }
}
