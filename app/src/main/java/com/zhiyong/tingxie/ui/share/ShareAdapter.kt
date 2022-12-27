package com.zhiyong.tingxie.ui.share

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zhiyong.tingxie.ui.UserRole

class ShareAdapter(activity: AppCompatActivity, private val itemsCount: Int, val quizId: Long)
  : FragmentStateAdapter(activity) {
  override fun getItemCount(): Int = itemsCount

  override fun createFragment(position: Int): Fragment {
    if (position == 0) {
      // todo: Replace with role from NetworkQuiz.
      return ShareIndividualFragment.newInstance(UserRole(quizId, EnumQuizRole.MEMBER))
//      shareIndividualFragment.arguments = bundle
//      return shareIndividualFragment
    }
    // todo: Replace with role from NetworkQuiz.
    val shareIndividualFragment = ShareIndividualFragment.newInstance(UserRole(quizId, EnumQuizRole.MEMBER))
//    shareIndividualFragment.arguments = bundle
    return shareIndividualFragment
  }
}
