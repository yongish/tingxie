package com.zhiyong.tingxie.ui.readings

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.databinding.RecyclerviewReadingsBinding
import com.zhiyong.tingxie.network.NetworkTitle

class ReadingsAdapter : RecyclerView.Adapter<ReadingsAdapter.ViewHolder>() {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    TODO("Not yet implemented")
  }

  override fun getItemCount(): Int {
    TODO("Not yet implemented")
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    TODO("Not yet implemented")
  }

  class ViewHolder(private val binding: RecyclerviewReadingsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(title: NetworkTitle) = with(binding) {

    }
  }
}
