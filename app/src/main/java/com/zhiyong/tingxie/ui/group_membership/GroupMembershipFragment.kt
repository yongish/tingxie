package com.zhiyong.tingxie.ui.group_membership

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.databinding.FragmentGroupMembershipBinding
import com.zhiyong.tingxie.ui.share.ShareFragment.Companion.EXTRA_EMAIL

class GroupMembershipFragment : Fragment() {

  companion object {
    fun newInstance() = GroupMembershipFragment()
  }

  private lateinit var viewModel: GroupMembershipViewModel

  private var _binding: FragmentGroupMembershipBinding? = null
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this).get(GroupMembershipViewModel::class.java)
    // TODO: Use the ViewModel
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentGroupMembershipBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val email = requireActivity().intent.getStringExtra(EXTRA_EMAIL)
    val viewModelFactory =
  }
}
