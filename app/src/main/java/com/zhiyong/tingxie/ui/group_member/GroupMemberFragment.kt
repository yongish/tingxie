package com.zhiyong.tingxie.ui.group_member

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentGroupMemberBinding
import com.zhiyong.tingxie.network.NetworkGroup
import com.zhiyong.tingxie.ui.group.GroupActivity

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
  private lateinit var name: String

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    val currentUser = FirebaseAuth.getInstance().currentUser!!
    email = currentUser.email!!
    name = currentUser.displayName!!

    _binding = FragmentGroupMemberBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val networkGroup = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requireActivity().intent.getParcelableExtra(
        GroupActivity.EXTRA_NETWORK_GROUP,
        NetworkGroup::class.java
      )
    } else {
      requireActivity().intent.getParcelableExtra(GroupActivity.EXTRA_NETWORK_GROUP)
    }
    val role = networkGroup?.role ?: "READ-ONLY"

    if (role == "READ-ONLY") {
      binding.fab.visibility = View.INVISIBLE
    } else {
      binding.fab.setOnClickListener { openAddGroupMemberDialog() }
    }

    // todo: Title bar should contain an option to delete the entire group. Show a confirmation modal.

    val viewModelFactory =
      GroupMemberViewModelFactory(requireActivity().application, networkGroup?.id ?: -1)
    viewModel =
      ViewModelProvider(this, viewModelFactory)[GroupMemberViewModel::class.java]
    val adapter = GroupMemberAdapter(
      requireActivity(), viewModel, binding.recyclerviewGroupMembers, role
    )
    binding.recyclerviewGroupMembers.adapter = adapter
    // Current user's role determines if she can see the share and delete imageViews.
    viewModel.groupMembers.observe(viewLifecycleOwner) {
      it?.let {
        adapter.groupMembers = it
        if (it.isEmpty()) {
          binding.emptyView.visibility = View.VISIBLE
        } else {
          binding.emptyView.visibility = View.INVISIBLE
        }
      }
    }

    val menuHost: MenuHost = requireActivity()
    menuHost.addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_group_member, menu)
//        if (role != "OWNER") {
//          menu.findItem(R.id.action_delete_group).isVisible = false
//        }
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (networkGroup == null) {
          return false
        }
        return when (menuItem.itemId) {
          R.id.action_delete_group -> {
            if (role == "OWNER") {
              if (networkGroup.numMembers == 1) {
                viewModel.deleteGroup(networkGroup.id, name, email)
                  .observe(viewLifecycleOwner) {
                    if (it > 0) {
                      startActivity(Intent(context, GroupActivity::class.java))
                    }
                  }
              } else {
                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle("Cannot leave")
                  .setMessage("You must transfer your group ownership to someone else before you leave")
                  .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
                  .create().show()
              }
            } else {
              val builder = AlertDialog.Builder(requireActivity())
              builder.setTitle("Leave group")
                .setMessage("Are you sure you want to leave the group?")
                .setPositiveButton("Yes") { dialog, _ ->
                  viewModel.deleteGroupMember(networkGroup.id, name, email, email)
                    .observe(viewLifecycleOwner) {
                      if (it > 0) {
                        dialog.dismiss()
                      }
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                .create().show()
            }
            true
          }
          else -> false
        }
      }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)

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