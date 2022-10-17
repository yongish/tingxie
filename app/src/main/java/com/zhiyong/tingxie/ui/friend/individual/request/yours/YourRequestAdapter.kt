package com.zhiyong.tingxie.ui.friend.individual.request.yours

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zhiyong.tingxie.databinding.RecyclerviewRequestIndividualYourBinding
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual


class YourRequestAdapter(
  val viewModel: YourViewModel,
  val recyclerView: RecyclerView
) : RecyclerView.Adapter<YourRequestAdapter.ViewHolder>() {

  var requests = listOf<TingXieIndividual>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(
      RecyclerviewRequestIndividualYourBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      )
    )

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val request = requests[position]
    holder.bind(request)
    holder.ivDelete.setOnClickListener {
      val adapterPosition = holder.adapterPosition
      Snackbar
        .make(recyclerView, "Removed ${request.email}", Snackbar.LENGTH_LONG)
        .setAction("Undo") {
          viewModel.addRequest(request)
          requests = requests.subList(0, adapterPosition) + request + requests.subList(
            adapterPosition,
            requests.size
          )
        }
        .show()
      viewModel.deleteRequest(request.email)
      requests = requests.subList(0, adapterPosition) + requests.subList(
        adapterPosition + 1,
        requests.size
      )
    }
  }

  override fun getItemCount(): Int = requests.size

  class ViewHolder(private val binding: RecyclerviewRequestIndividualYourBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val ivDelete = binding.ivDelete

    fun bind(request: TingXieIndividual) = with(binding) {
      tvEmail.text = request.email
    }
  }
}
