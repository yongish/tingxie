package com.zhiyong.tingxie.ui.share

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.RecyclerviewFriendGroupNameBinding
import com.zhiyong.tingxie.ui.friend.FriendGroupMemberActivity
import com.zhiyong.tingxie.ui.friend.FriendGroupNameAdapter
import com.zhiyong.tingxie.ui.friend.TingXieGroup

class ShareGroupNameAdapter(private val quizId: Long,
                            private val groups: List<TingXieShareGroup>,
                            private val context: Context,
                            val viewModel: ShareGroupNameViewModel,
                            val recyclerView: RecyclerView)
  : RecyclerView.Adapter<ShareGroupNameAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
      ViewHolder(RecyclerviewFriendGroupNameBinding.inflate(
          LayoutInflater.from(parent.context), parent, false
      ))

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val group = groups[position]
    holder.bind(group)
    holder.clIdentifier.setOnClickListener {
      val intent = Intent(context, FriendGroupMemberActivity::class.java)
      intent.putExtra(
          FriendGroupNameAdapter.EXTRA_GROUP, TingXieGroup(group.name, group.members)
      )
      context.startActivity(intent)
    }
    holder.ivDelete.setOnClickListener {
      val adapterPosition = holder.adapterPosition
      Snackbar
          .make(recyclerView, "Removed ${group.name}", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            viewModel.addShareGroup(quizId, group.name)
            notifyItemInserted(adapterPosition)
          }
          .show()
      viewModel.deleteShareGroup(quizId, group.name)
      notifyItemRemoved(adapterPosition)
    }
  }

  override fun getItemCount(): Int = groups.size

  class ViewHolder(private val binding: RecyclerviewFriendGroupNameBinding)
    : RecyclerView.ViewHolder(binding.root) {
    val clIdentifier = binding.clIdentifier
    val ivDelete = binding.ivDelete

    fun bind(group: TingXieShareGroup) = with(binding) {
      tvName.text = group.name
      tvMemberCount.text = String.format(
          itemView.context.getString(R.string.member_count), group.members.size
      )
    }
  }
}