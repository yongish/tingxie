package com.zhiyong.tingxie.ui.group_member

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentGroupMemberBinding
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.ui.EXTRA_ROLE
import com.zhiyong.tingxie.ui.UserRole
import com.zhiyong.tingxie.ui.add_group_member.AddGroupMemberActivity
import com.zhiyong.tingxie.ui.group.GroupActivity
import com.zhiyong.tingxie.ui.group_member.SelectRoleFragment.Companion.REQUEST_KEY
import com.zhiyong.tingxie.ui.share.EnumQuizRole

class GroupMemberFragment : Fragment() {

  companion object {
    fun newInstance() = GroupMemberFragment()
    const val EXTRA_SHOW_OWNER_OPTION =
      "com.zhiyong.tingxie.ui.group_member.extra.SHOW_OWNER_OPTION"
    const val EXTRA_GROUP_MEMBER =
      "com.zhiyong.tingxie.ui.group_member.extra.GROUP_MEMBER"
    const val EXTRA_POSITION = "com.zhiyong.tingxie.ui.group_member.extra.POSITION"
  }

  private lateinit var adapter: GroupMemberAdapter

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

    val userRole = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requireActivity().intent.getParcelableExtra(
        EXTRA_ROLE,
        UserRole::class.java
      )
    } else {
      requireActivity().intent.getParcelableExtra(EXTRA_ROLE)
    }
    if (userRole == null) {
      binding.otherErrorView.visibility = View.VISIBLE
    }

    val role = userRole?.role ?: EnumQuizRole.MEMBER

    if (role == EnumQuizRole.MEMBER) {
      binding.fab.visibility = View.INVISIBLE
    } else {
      binding.fab.setOnClickListener {
        val intent = Intent(context, AddGroupMemberActivity::class.java)
        intent.putExtra(EXTRA_ROLE, userRole)
        startActivity(intent)
      }
    }

    val viewModelFactory =
      GroupMemberViewModelFactory(requireActivity().application, userRole?.id ?: -1)
    val viewModel =
      ViewModelProvider(this, viewModelFactory)[GroupMemberViewModel::class.java]
    adapter = GroupMemberAdapter(
      requireActivity(),
      viewModel,
      binding.recyclerviewGroupMembers,
      viewLifecycleOwner,
      role,
      userRole?.id ?: -1
    )
    binding.recyclerviewGroupMembers.adapter = adapter

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

    setFragmentResultListener(REQUEST_KEY) { _, bundle ->
      val groupMember = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getParcelable(EXTRA_GROUP_MEMBER, NetworkGroupMember::class.java)
      } else {
        bundle.getParcelable(EXTRA_GROUP_MEMBER)
      }
      val position = bundle.getInt(EXTRA_POSITION)
      if (groupMember != null) {
        viewModel.changeRole(userRole?.id, groupMember.email, groupMember.role)
          .observe(viewLifecycleOwner) {
            if (it > 0) adapter.changeRole(groupMember, position)
            if (role == EnumQuizRole.OWNER && groupMember.role == EnumQuizRole.OWNER.name) {
              viewModel.changeRole(userRole?.id, email, "ADMIN")
            }
          }
      }
    }

    val menuHost: MenuHost = requireActivity()
    menuHost.addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_group_member, menu)
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (userRole == null) {
          return false
        }
        return when (menuItem.itemId) {
          R.id.action_delete_group -> {
            if (role == EnumQuizRole.OWNER) {
              if (adapter.groupMembers.size == 1) {
                viewModel.deleteGroup(userRole.id, name, email)
                  .observe(viewLifecycleOwner) {
                    if (it > -1) {
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
                  viewModel.deleteGroupMember(userRole.id, name, email, email)
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
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
