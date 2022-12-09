package com.zhiyong.tingxie.ui.group_member

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.RecyclerviewGroupMemberBinding
import com.zhiyong.tingxie.network.NetworkGroupMember

class GroupMemberAdapter(
  private val context: Context,
  val viewModel: GroupMemberViewModel,
  val recyclerView: RecyclerView,
  val role: String
) : RecyclerView.Adapter<GroupMemberAdapter.ViewHolder>() {

  val user = FirebaseAuth.getInstance().currentUser
  val email = user?.email

  var groupMembers = listOf<NetworkGroupMember>()
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

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val spannableString = SpannableString(
      "To share a quiz with a group, go to that quiz and tap on the @ icon."
    )
    val d: Drawable? = ResourcesCompat.getDrawable(
      context.resources, R.drawable.ic_baseline_share_black_24, null
    )
    d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
    spannableString.setSpan(
      d?.let {
        ImageSpan(it, ImageSpan.ALIGN_BOTTOM)
      },
      spannableString.toString().indexOf("@"),
      spannableString.toString().indexOf("@") + 1,
      Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )

    val groupMember = groupMembers[position]
    holder.bind(groupMember)
    holder.clIdentifier.setOnClickListener {
      val builder = AlertDialog.Builder(context)
      builder.setMessage(spannableString)
        .setNegativeButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
        .create().show()
    }

    if (role == "READ-ONLY") {
      holder.ivDelete.visibility = View.INVISIBLE
    } else {
      holder.ivDelete.setOnClickListener {
//      val intent = Intent(context, GroupMemberActivity::class.java)
//      intent.putExtra(EXTRA_NETWORK_GROUP, group)
//      context.startActivity(intent)
        if (groupMember.email == email && groupMember.role == "OWNER") {
          AlertDialog.Builder(context)
            .setTitle("Removal not allowed")
            .setMessage("You must appoint someone else as the group owner before removing yourself.")
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
        } else {
          AlertDialog.Builder(context)
            .setTitle("Remove ${groupMember.userName}?")
            .setMessage("Are you sure you want to remove ${groupMember.userName} from this group?")
            .setPositiveButton("Yes") { dialog, _ ->

            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel()}
            .create().show()
        }

      }
    }
  }

  override fun getItemCount(): Int = groupMembers.size

  class ViewHolder(private val binding: RecyclerviewGroupMemberBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val clIdentifier = binding.clIdentifier
    val ivDelete = binding.ivDelete

    fun bind(groupMember: NetworkGroupMember) = with(binding) {
      tvName.text = groupMember.userName
      tvEmail.text = groupMember.email
      tvRole.text = groupMember.role
    }
  }
}
