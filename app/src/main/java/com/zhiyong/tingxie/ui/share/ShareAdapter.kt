package com.zhiyong.tingxie.ui.share

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ShareAdapter(activity: AppCompatActivity, private val itemsCount: Int, val quizId: Long)
  : FragmentStateAdapter(activity) {
  override fun getItemCount(): Int = itemsCount

  override fun createFragment(position: Int): Fragment {
    if (position == 0) {
      return ShareIndividualFragment.newInstance(quizId)
//      shareIndividualFragment.arguments = bundle
//      return shareIndividualFragment
    }
    val shareIndividualFragment = ShareIndividualFragment.newInstance(quizId)
//    shareIndividualFragment.arguments = bundle
    return shareIndividualFragment
  }
}
