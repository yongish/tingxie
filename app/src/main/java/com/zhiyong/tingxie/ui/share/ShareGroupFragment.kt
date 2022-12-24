package com.zhiyong.tingxie.ui.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zhiyong.tingxie.databinding.ShareGroupFragmentBinding

class ShareGroupFragment : Fragment() {

  companion object {
    fun newInstance() = ShareGroupFragment()
  }

  private var _binding: ShareGroupFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = ShareGroupFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }


}
