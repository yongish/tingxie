package com.zhiyong.tingxie.ui.friend

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.RecyclerviewGroupNameBinding

class GroupNameAdapter(private val groups: List<TingXieGroup>,
                       private val context: Context,
                       val viewModel: GroupViewModel,
                       val recyclerView: RecyclerView)
  : RecyclerView.Adapter<GroupNameAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
      ViewHolder(RecyclerviewGroupNameBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      ))

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val group = groups[position]
    holder.bind(group)
    holder.clIdentifier.setOnClickListener {
      // Open GroupMember activity.
      context.startActivity(Intent(context, GroupMemberActivity::class.java))
    }
    holder.ivDelete.setOnClickListener {
      val adapterPosition = holder.adapterPosition
      Snackbar
          .make(recyclerView, "Removed ${group.name}", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            viewModel.addGroup(group)
            notifyItemInserted(adapterPosition)
          }
          .show()
      viewModel.deleteGroup(group.name)
      notifyItemRemoved(adapterPosition)
    }
  }

  override fun getItemCount(): Int = groups.size

  class ViewHolder(private val binding: RecyclerviewGroupNameBinding)
    : RecyclerView.ViewHolder(binding.root) {
    val clIdentifier = binding.clIdentifier
    val ivDelete = binding.ivDelete

    fun bind(group: TingXieGroup) = with(binding) {
      tvName.text = group.name
      tvMemberCount.text = String.format(
          itemView.context.getString(R.string.member_count), group.individuals.size
      )
    }
  }
}
