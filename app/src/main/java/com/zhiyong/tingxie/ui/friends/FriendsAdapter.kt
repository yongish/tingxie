package com.zhiyong.tingxie.ui.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.RecyclerviewFriendBinding

class FriendsAdapter(private val friends: List<TingXieFriend>, private var context: Context)
//class FriendsAdapter(private val friends: List<TingXieFriend>)
  : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

  private lateinit var mFriends: List<TingXieFriend>
//  private var _binding: RecyclerviewFriendBinding? = null
//  private val binding get() = _binding!!

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = RecyclerviewFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//    _binding = RecyclerviewFriendBinding.inflate(LayoutInflater.from(parent.context))
//    return ViewHolder(binding)

    return ViewHolder.from(parent, context)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(friends[position])
//    holder.bind(mFriends[position])
  }

  override fun getItemCount(): Int = friends.size

  fun setEmailItems(friends: List<TingXieFriend>) {
    mFriends = friends
    notifyDataSetChanged()
  }

//  class ViewHolder(private val binding: RecyclerviewFriendBinding) : RecyclerView.ViewHolder(binding.root) {
  class ViewHolder private constructor(view: View, val context: Context) : RecyclerView.ViewHolder(view) {
//    val binding = RecyclerviewFriendBinding.inflate(LayoutInflater.from(context))
    private val tvEmail: TextView = view.findViewById(R.id.tvEmail)

    fun bind(friend: TingXieFriend) {
      tvEmail.text = friend.email
//      binding.tvEmail.text = friend.email
    }

    companion object {
      fun from(parent: ViewGroup, context: Context): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recyclerview_friend, parent, false)
        return ViewHolder(view, context)
      }
    }
  }
}
