package com.zhiyong.tingxie.ui.share

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.zhiyong.tingxie.databinding.RecyclerviewShareBinding
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.ui.group_member.SelectRoleFragment
import com.zhiyong.tingxie.ui.group_membership.GroupMembershipActivity
import com.zhiyong.tingxie.ui.share.ShareFragment.Companion.EXTRA_EMAIL

class ShareAdapter(
  private val context: Context,
  val viewModel: ShareViewModel,
  val recyclerView: RecyclerView,
  private val viewLifecycleOwner: LifecycleOwner,
  private val quizId: Long,
  private val role: EnumQuizRole
) : RecyclerView.Adapter<ShareAdapter.ViewHolder>() {

  val user = FirebaseAuth.getInstance().currentUser
  val name = user?.displayName
  val email = user?.email
  var editing = false

  var users = mutableListOf<NetworkGroupMember>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      RecyclerviewShareBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      )
    )
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val user = users[position]
    holder.bind(user)
//    if (role == EnumQuizRole.OWNER || role == EnumQuizRole.MEMBER) {
    if (role == EnumQuizRole.OWNER) {
      holder.ivEditRole.visibility = View.INVISIBLE
    }
    if (role == EnumQuizRole.MEMBER) {
      holder.ivDelete.visibility = View.INVISIBLE
    } else {
      holder.clIdentifier.setOnClickListener {
        if (user.role == EnumQuizRole.OWNER.name && users.filter { it.role == EnumQuizRole.OWNER.name }.size == 1) {
          AlertDialog.Builder(context)
            .setTitle("Role change not allowed")
            .setMessage("You must appoint someone else as the quiz owner before changing your own role.")
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .create().show()
        } else {
          val fm = (context as AppCompatActivity).supportFragmentManager
          val selectRoleFragment: SelectRoleFragment =
            SelectRoleFragment.newInstance(
              user.role != EnumQuizRole.OWNER.name,
              user,
              position
            )
          selectRoleFragment.show(fm, "fragment_select_role")
        }
      }

      holder.ivDelete.setOnClickListener {
        if (user.email == email && user.role == EnumQuizRole.OWNER.name) {
          AlertDialog.Builder(context)
            .setTitle("Removal not allowed")
            .setMessage("You must appoint someone else as the quiz owner before removing yourself.")
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .create().show()
        } else {
          AlertDialog.Builder(context)
            .setTitle("Remove ${user.userName}?")
            .setMessage("Are you sure you want to remove ${user.userName} from this quiz?")
            .setPositiveButton("Yes") { _, _ ->
              viewModel.removeQuizMember(
                quizId,
                name,
                email,
                user.email
              ).observe(viewLifecycleOwner) {
                if (it.toInt() >= -1) {
                  // Backend returns -1 if there is no token found for user, but the user
                  // is removed anyway.
                  users.removeAt(position)
                  notifyItemChanged(position)
                }
              }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .create().show()
        }
      }
    }

    holder.btnGroupMemberships.setOnClickListener {
      val intent = Intent(context, GroupMembershipActivity::class.java)
      intent.putExtra(EXTRA_EMAIL, user.email)
      context.startActivity(intent)
    }

  }

  fun changeRole(networkGroupMember: NetworkGroupMember, i: Int) {
    users[i] = networkGroupMember
    notifyItemChanged(i)
  }

  override fun getItemCount(): Int = users.size

  class ViewHolder(private val binding: RecyclerviewShareBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val clIdentifier = binding.clIdentifier
    val ivEditRole = binding.ivEditRole
    val ivDelete = binding.ivDelete
    val btnGroupMemberships = binding.btnGroupMemberships

    fun bind(groupMember: NetworkGroupMember) = with(binding) {
      tvName.text = groupMember.userName
      tvEmail.text = groupMember.email
      tvRole.text = groupMember.role
    }
  }
}
