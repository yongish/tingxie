package com.zhiyong.tingxie.ui.share

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.databinding.RecyclerviewGroupNameBinding
import com.zhiyong.tingxie.ui.friend.TingXieGroup

class ShareGroupNameAdapter(private val groups: List<TingXieGroup>,
                            private val context: Context,
                            val viewModel: ShareGroupViewModel,
                            val recyclerView: RecyclerView)
  : RecyclerView.Adapter<ShareGroupNameAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    TODO("Not yet implemented")
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    TODO("Not yet implemented")
  }

  override fun getItemCount(): Int = groups.size

  class ViewHolder(private val binding: RecyclerviewGroupNameBinding)
    : RecyclerView.ViewHolder(binding.root) {

  }
}