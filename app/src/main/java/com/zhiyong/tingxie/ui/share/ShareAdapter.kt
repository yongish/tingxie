package com.zhiyong.tingxie.ui.share

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.zhiyong.tingxie.ui.main.MainActivity
import com.zhiyong.tingxie.ui.share.ShareFragment.Companion.EXTRA_EMAIL

class ShareAdapter(
  private val context: Context,
  val viewModel: ShareViewModel,
  val recyclerView: RecyclerView,
  private val viewLifecycleOwner: LifecycleOwner,
  private val quizId: Long,
  private var role: EnumQuizRole
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

  private fun shouldShowOwner(): Boolean =
    users.find { user -> user.email == email }?.role == EnumQuizRole.OWNER.name

  @SuppressLint("ClickableViewAccessibility")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val user = users[position]
    holder.bind(user)

    if (role == EnumQuizRole.ADMIN || role == EnumQuizRole.OWNER) {
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
              ::shouldShowOwner,
              user,
              position
            )
          selectRoleFragment.show(fm, "fragment_select_role")
        }
      }
      holder.ivEditRole.visibility = View.VISIBLE
      holder.ivDelete.visibility = View.VISIBLE
    }
    if (user.role == EnumQuizRole.OWNER.name || role == EnumQuizRole.MEMBER) {
      holder.clIdentifier.setOnClickListener {}
      holder.ivEditRole.visibility = View.INVISIBLE
      holder.ivDelete.visibility = View.INVISIBLE
    } else {
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
                  if (user.email == email) {
                    context.startActivity(Intent(context, MainActivity::class.java))
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

    holder.btnGroupMemberships.setOnClickListener {
      val intent = Intent(context, GroupMembershipActivity::class.java)
      intent.putExtra(EXTRA_EMAIL, user.email)
      context.startActivity(intent)
    }
  }

  fun changeRole(networkGroupMember: NetworkGroupMember, i: Int) {
    if (networkGroupMember.email == email) {
      role = EnumQuizRole.valueOf(networkGroupMember.role)
    }

    users[i] = networkGroupMember
    notifyDataSetChanged()
  }

  fun changeCurrentUserToAdmin() {
    // Find owner position in adapter.
    val ownerIndex = users.indexOfFirst { user -> user.email == email }
    users[ownerIndex].role = EnumQuizRole.ADMIN.name
    notifyItemChanged(ownerIndex)
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
