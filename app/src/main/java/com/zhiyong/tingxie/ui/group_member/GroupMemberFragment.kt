package com.zhiyong.tingxie.ui.group_member

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentGroupMemberBinding
import com.zhiyong.tingxie.network.NetworkGroup
import com.zhiyong.tingxie.ui.group.GroupActivity
import com.zhiyong.tingxie.ui.share.ShareActivity

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GroupMemberFragment : Fragment() {

  companion object {
    fun newInstance() = GroupMemberFragment()
  }

  private lateinit var viewModel: GroupMemberViewModel
  private var _binding: FragmentGroupMemberBinding? = null
  private val binding get() = _binding!!

  private lateinit var email: String

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    email = FirebaseAuth.getInstance().currentUser?.email!!

    _binding = FragmentGroupMemberBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // todo: Use this to get the user's role, to set the delete and edit imageViews as visible or invisible.
    val networkGroup = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requireActivity().intent.getParcelableExtra(GroupActivity.EXTRA_NETWORK_GROUP, NetworkGroup::class.java)
    } else {
      requireActivity().intent.getParcelableExtra(GroupActivity.EXTRA_NETWORK_GROUP)
    }

    binding.fab.setOnClickListener { openAddGroupMemberDialog() }

    // todo: Title bar should contain an option to delete the entire group. Show a confirmation modal.

    viewModel = ViewModelProvider(this)[GroupMemberViewModel::class.java]

    // Current user's role determines if she can see the share and delete imageViews.
    viewModel.groupMembers.observe(viewLifecycleOwner) { it ->
      val role = it.first { it.email == email }.role

      it?.let {


      }
    }

    val adapter = GroupMemberAdapter(requireActivity(), viewModel, binding.recyclerviewGroupMembers, role)

//    binding.buttonFirst.setOnClickListener {
//      findNavController().navigate(R.id.action_FirstFragment_to_Second2Fragment)
//    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  // todo: Add a member by QR code.
  // next step is to add a fragment.
  private fun openAddGroupMemberDialog() {


    val builder = AlertDialog.Builder(requireActivity())
    builder.setTitle("Add group member")
      .setMessage("Enter a user's email address to add her/him to the group.")
  }
}