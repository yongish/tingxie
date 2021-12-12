package com.zhiyong.tingxie.ui.friend.group.requests.yours

import androidx.fragment.app.Fragment

class YourRequestsFragment : Fragment() {

  companion object {
    fun newInstance() = YourRequestsFragment()
  }

  private lateinit var viewModel: YourRequestsViewModel

  override fun onDestroyView() {
    super.onDestroyView()
//    _binding = null
  }
}