package com.zhiyong.tingxie.ui.friend.group.requests.yours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.databinding.YourGroupRequestFragmentBinding

class YourGroupRequestFragment : Fragment() {

  companion object {
    fun newInstance() = YourGroupRequestFragment()
  }

  private lateinit var viewModel: YourGroupRequestViewModel
  private var _binding: YourGroupRequestFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    _binding = YourGroupRequestFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel = ViewModelProvider(this)[YourGroupRequestViewModel::class.java]
    viewModel.requests.observe(viewLifecycleOwner, { requests -> requests?.apply {
      requests?.apply {
//        binding.recyclerviewYourGroupRequests.adapter = Your
      }
    } })
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}