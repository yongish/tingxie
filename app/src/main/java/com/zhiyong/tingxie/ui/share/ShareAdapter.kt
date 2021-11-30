package com.zhiyong.tingxie.ui.share

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zhiyong.tingxie.databinding.RecyclerviewShareBinding

class ShareAdapter(private val quizId: Long,
                   private val shares: List<TingXieShare>,
                   private val context: Context,
                   val viewModel: ShareViewModel,
                   val recyclerView: RecyclerView)
  : RecyclerView.Adapter<ShareAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(RecyclerviewShareBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val share = shares[position]
    holder.bind(share)

    holder.ivDelete.setOnClickListener {
      val adapterPosition = holder.adapterPosition
      Snackbar
          .make(recyclerView, "Removed ${share.firstName}", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            viewModel.addShare(share)
            notifyItemInserted(adapterPosition)
          }
          .show()
      viewModel.deleteShare(quizId, share.email)
      notifyItemRemoved(adapterPosition)
    }

    val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, EnumQuizRole.values().map { role -> role.toString() })
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    holder.spRole.adapter = adapter
    holder.spRole.setSelection(EnumQuizRole.values().indexOf(share.role))
//    if ()
//    holder.spRole.isEnabled = false
  }

  override fun getItemCount(): Int = shares.size

  class ViewHolder(private val binding: RecyclerviewShareBinding)
    : RecyclerView.ViewHolder(binding.root) {
    val ivDelete = binding.ivDelete
    val spRole = binding.spRole

    fun bind(share: TingXieShare) = with(binding) {
      tvEmail.text = share.email
    }
  }
}
