package com.zhiyong.tingxie.ui.group_membership

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.databinding.FragmentGroupMembershipBinding
import com.zhiyong.tingxie.ui.group.GroupAdapter
import com.zhiyong.tingxie.ui.share.ShareFragment.Companion.EXTRA_EMAIL

class GroupMembershipFragment : Fragment() {

  companion object {
    fun newInstance() = GroupMembershipFragment()
  }

  private var _binding: FragmentGroupMembershipBinding? = null
  private val binding get() = _binding!!

  private lateinit var email: String
  private lateinit var name: String

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val currentUser = FirebaseAuth.getInstance().currentUser!!
    email = currentUser.email!!
    name = currentUser.displayName!!

    _binding = FragmentGroupMembershipBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val viewModelFactory = GroupMembershipViewModelFactory(
      requireActivity().application,
      requireActivity().intent.getStringExtra(EXTRA_EMAIL) ?: this.email
    )
    val viewModel = ViewModelProvider(this, viewModelFactory)[GroupMembershipViewModel::class.java]
    val adapter = GroupAdapter(requireActivity(), viewModel, binding.recyclerviewGroupMemberships)
    binding.recyclerviewGroupMemberships.adapter = adapter
    viewModel.groups.observe(viewLifecycleOwner) {
      it?.let {
        adapter.groups = it
        if (it.isEmpty()) {
          binding.emptyView.visibility = View.VISIBLE
        } else {
          binding.emptyView.visibility = View.INVISIBLE
        }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
