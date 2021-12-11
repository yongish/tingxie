package com.zhiyong.tingxie.ui.share

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ShareAdapter(activity: AppCompatActivity, private val itemsCount: Int, val bundle: Bundle)
  : FragmentStateAdapter(activity) {
  override fun getItemCount(): Int = itemsCount

  override fun createFragment(position: Int): Fragment {
    if (position == 0) {
      val shareIndividualFragment = ShareIndividualFragment.newInstance()
      shareIndividualFragment.arguments = bundle
      return shareIndividualFragment
    }
    val shareGroupFragment = ShareGroupNameFragment.newInstance()
    shareGroupFragment.arguments = bundle
    return ShareGroupNameFragment.newInstance()
  }
}