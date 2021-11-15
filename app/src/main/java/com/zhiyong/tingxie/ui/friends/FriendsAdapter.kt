package com.zhiyong.tingxie.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.databinding.RecyclerviewFriendBinding

class FriendsAdapter(private val friends: List<TingXieFriend>)
  : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(RecyclerviewFriendBinding.inflate(
      LayoutInflater.from(parent.context), parent, false
    ))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(friends[position])
  }

  override fun getItemCount(): Int = friends.size

  class ViewHolder(private val binding: RecyclerviewFriendBinding)
    : RecyclerView.ViewHolder(binding.root) {
    fun bind(friend: TingXieFriend) {
      binding.tvEmail.text = friend.email
    }
  }
}
