package com.zhiyong.tingxie.ui.connect

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ProfileFragmentBinding

class FriendConnectFragment : Fragment() {

  companion object {
    fun newInstance() = FriendConnectFragment()
  }

  private lateinit var viewModel: FriendConnectViewModel
  private var _binding: ProfileFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = ProfileFragmentBinding.inflate(inflater, container, false)
    return inflater.inflate(R.layout.profile_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // Get email.
    binding.myEmail.text = "yongish@gmail.com"

    viewModel = ViewModelProvider(this).get(FriendConnectViewModel::class.java)
    // TODO: Use the ViewModel
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}