package com.zhiyong.tingxie.ui.friend.group.requests.yours

import androidx.fragment.app.Fragment

class YourGroupRequestFragment : Fragment() {

  companion object {
    fun newInstance() = YourGroupRequestFragment()
  }

  private lateinit var viewModel: YourGroupRequestViewModel

  override fun onDestroyView() {
    super.onDestroyView()
//    _binding = null
  }
}