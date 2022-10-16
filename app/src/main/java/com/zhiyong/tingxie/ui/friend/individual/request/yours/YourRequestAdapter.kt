package com.zhiyong.tingxie.ui.friend.individual.request.yours

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zhiyong.tingxie.databinding.RecyclerviewRequestIndividualYourBinding
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual

class YourRequestAdapter(private val requests: List<TingXieIndividual>,
                         val viewModel: YourViewModel,
                         val recyclerView: RecyclerView)
  : RecyclerView.Adapter<YourRequestAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
      ViewHolder(RecyclerviewRequestIndividualYourBinding.inflate(
          LayoutInflater.from(parent.context), parent, false
      ))

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val request = requests[position]
    holder.bind(request)
    holder.ivDelete.setOnClickListener {
      val adapterPosition = holder.adapterPosition
      Snackbar
          .make(recyclerView, "Removed ${request.email}", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            viewModel.addRequest(request)
            notifyItemInserted(adapterPosition)
          }
          .show()
      viewModel.deleteRequest(request.email)
      notifyItemRemoved(adapterPosition)
    }
  }

  override fun getItemCount(): Int = requests.size

  class ViewHolder(private val binding: RecyclerviewRequestIndividualYourBinding)
    : RecyclerView.ViewHolder(binding.root) {
    val ivDelete = binding.ivDelete

    fun bind(request: TingXieIndividual) = with(binding) {
      tvEmail.text = request.email
    }
  }
}
