package com.zhiyong.tingxie.ui.group_member

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.databinding.RecyclerviewGroupMemberBinding
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.ui.group.GroupActivity
import com.zhiyong.tingxie.ui.share.EnumQuizRole

class GroupMemberAdapter(
  private val context: Context,
  val viewModel: GroupMemberViewModel,
  val recyclerView: RecyclerView,
  private val viewLifecycleOwner: LifecycleOwner,
  private val groupId: Long,
  var role: EnumQuizRole
) : RecyclerView.Adapter<GroupMemberAdapter.ViewHolder>() {

  val user = FirebaseAuth.getInstance().currentUser
  val email = user?.email

  var groupMembers = mutableListOf<NetworkGroupMember>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(
      RecyclerviewGroupMemberBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )

  private fun shouldShowOwner(): Boolean {
    val currMember = groupMembers.find { groupMember -> groupMember.email == email }
    return currMember?.role == EnumQuizRole.OWNER.name
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val groupMember = groupMembers[position]
    holder.bind(groupMember)

    if (role == EnumQuizRole.ADMIN || role == EnumQuizRole.OWNER) {
      holder.clIdentifier.setOnClickListener {
        val fm = (context as AppCompatActivity).supportFragmentManager
        val selectRoleFragment: SelectRoleFragment =
          SelectRoleFragment.newInstance(::shouldShowOwner, groupMember, position)
        selectRoleFragment.show(fm, "fragment_select_role")
      }
      holder.ivEditRole.visibility = View.VISIBLE
      holder.ivDelete.visibility = View.VISIBLE
    }
    if (groupMember.role == EnumQuizRole.OWNER.name || role == EnumQuizRole.MEMBER) {
      holder.clIdentifier.setOnClickListener {}
      holder.ivEditRole.visibility = View.INVISIBLE
      holder.ivDelete.visibility = View.INVISIBLE
    } else {
      holder.ivDelete.setOnClickListener {
        if (groupMember.email == email && groupMember.role == EnumQuizRole.OWNER.name) {
          AlertDialog.Builder(context)
            .setTitle("Removal not allowed")
            .setMessage("You must appoint someone else as the group owner before removing yourself.")
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .create().show()
        } else {
          AlertDialog.Builder(context)
            .setTitle("Remove ${groupMember.userName}?")
            .setMessage("Are you sure you want to remove ${groupMember.userName} from this group?")
            .setPositiveButton("Yes") { dialog, _ ->
              viewModel.deleteGroupMember(
                groupId,
                user?.displayName,
                email,
                groupMember.email
              ).observe(viewLifecycleOwner) {
                if (it > 0) {
                  groupMembers.removeAt(position)
                  if (groupMember.email == email) {
                    context.startActivity(Intent(context, GroupActivity::class.java))
                  } else {
                    notifyItemChanged(position)
                  }
                }
              }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .create().show()
        }
      }
    }
  }

  fun changeRole(networkGroupMember: NetworkGroupMember, i: Int) {
    if (networkGroupMember.email == email) {
      role = EnumQuizRole.valueOf(networkGroupMember.role)
    }

    groupMembers[i] = networkGroupMember
    notifyDataSetChanged()
  }

  fun changeCurrentUserToAdmin() {
    role = EnumQuizRole.OWNER

    // Find owner position in adapter.
    val ownerIndex =
      groupMembers.indexOfFirst { networkGroupMember -> networkGroupMember.email == email }
    groupMembers[ownerIndex].role = EnumQuizRole.ADMIN.name
    notifyItemChanged(ownerIndex)
  }

  override fun getItemCount(): Int = groupMembers.size

  class ViewHolder(private val binding: RecyclerviewGroupMemberBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val clIdentifier = binding.clIdentifier
    val ivEditRole = binding.ivEditRole
    val ivDelete = binding.ivDelete

    fun bind(groupMember: NetworkGroupMember) = with(binding) {
      tvName.text = groupMember.userName
      tvEmail.text = groupMember.email
      tvRole.text = groupMember.role
    }
  }
}
