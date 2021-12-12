package com.zhiyong.tingxie.ui.friend.group.member

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.RecyclerviewFriendGroupMemberBinding
import com.zhiyong.tingxie.ui.friend.group.name.TingXieFriendGroup
import com.zhiyong.tingxie.ui.share.EnumQuizRole

class FriendGroupMemberAdapter(private val group: TingXieFriendGroup,
                               private val context: Context,
                               val viewModel: FriendGroupMemberViewModel,
                               val recyclerView: RecyclerView)
  : RecyclerView.Adapter<FriendGroupMemberAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(RecyclerviewFriendGroupMemberBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ))

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val member = group.members[position]
    holder.bind(member)

    val adapter = ArrayAdapter(
        context,
        android.R.layout.simple_spinner_item,
        EnumQuizRole.values().map { role -> role.toString() }
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    holder.spRole.adapter = adapter
    holder.spRole.setSelection(EnumQuizRole.values().indexOf(member.role))

    holder.ivDelete.setOnClickListener {
      val adapterPosition = holder.adapterPosition
      Snackbar
          .make(recyclerView,
              "Removed ${member.firstName + " " + member.lastName}",
              Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            viewModel.add(group.name, member)
            notifyItemInserted(adapterPosition)
          }
          .show()
      viewModel.delete(group.name, member.email)
      notifyItemRemoved(adapterPosition)
    }
  }

  override fun getItemCount(): Int = group.members.size

  class ViewHolder(private val binding: RecyclerviewFriendGroupMemberBinding)
    : RecyclerView.ViewHolder(binding.root) {
    val spRole = binding.spRole
    val ivDelete = binding.ivDelete

    fun bind(member: TingXieGroupMember) = with(binding) {
      tvEmail.text = member.email
      tvName.text = String.format(
          itemView.context.getString(R.string.username),
          member.lastName,
          member.firstName,
      )

    }

  }

}