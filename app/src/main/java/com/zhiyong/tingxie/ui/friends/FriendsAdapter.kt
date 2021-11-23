package com.zhiyong.tingxie.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zhiyong.tingxie.databinding.RecyclerviewFriendBinding

class FriendsAdapter(private val friends: List<TingXieFriend>,
                     val viewModel: FriendsViewModel,
                     val recyclerView: RecyclerView
                     )
  : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(RecyclerviewFriendBinding.inflate(
      LayoutInflater.from(parent.context), parent, false
    ))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val friend = friends[position]
    holder.bind(friend)
    holder.ivDelete.setOnClickListener {
      val adapterPosition = holder.adapterPosition
      Snackbar
        .make(recyclerView, "Removed friend", Snackbar.LENGTH_LONG)
        .setAction("Undo") {
          viewModel.addFriend(friend)
          notifyItemInserted(adapterPosition)
        }
        .show()
      viewModel.deleteFriend(friend.email)
      notifyItemRemoved(adapterPosition)
    }
  }

  override fun getItemCount(): Int = friends.size

  fun onFriendRemove(viewHolder: RecyclerView.ViewHolder) {
    viewModel.deleteFriend(friends[viewHolder.adapterPosition].email)
  }

  class ViewHolder(private val binding: RecyclerviewFriendBinding)
    : RecyclerView.ViewHolder(binding.root) {
    val ivDelete = binding.ivDelete

    fun bind(friend: TingXieFriend) = with(binding) {
      tvEmail.text = friend.email
    }
//    fun bind(friend: TingXieFriend) {
//      binding.tvEmail.text = friend.email
//    }
  }
}
