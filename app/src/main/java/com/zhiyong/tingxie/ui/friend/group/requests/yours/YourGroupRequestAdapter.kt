package com.zhiyong.tingxie.ui.friend.group.requests.yours

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.databinding.RecyclerviewRequestGroupYourBinding

class YourGroupRequestAdapter(private val requests: List<TingXieYourGroupRequest>,
                              val viewModel: YourGroupRequestViewModel,
                              val recyclerView: RecyclerView)
  : RecyclerView.Adapter<YourGroupRequestAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    TODO("Not yet implemented")
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    TODO("Not yet implemented")
  }

  override fun getItemCount(): Int {
    TODO("Not yet implemented")
  }

  class ViewHolder(private val binding: RecyclerviewRequestGroupYourBinding)
    : RecyclerView.ViewHolder(binding.root) {

  }
}