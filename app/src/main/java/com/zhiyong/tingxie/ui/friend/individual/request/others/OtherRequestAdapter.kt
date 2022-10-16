package com.zhiyong.tingxie.ui.friend.individual.request.others

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.RecyclerviewRequestIndividualOtherBinding
import com.zhiyong.tingxie.ui.friend.individual.TingXieIndividual

class OtherRequestAdapter(
  private val requests: List<TingXieIndividual>,
  private val context: Context,
  val viewModel: OtherViewModel,
  val recyclerView: RecyclerView
) : RecyclerView.Adapter<OtherRequestAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(
      RecyclerviewRequestIndividualOtherBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      )
    )

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val request = requests[position]

    holder.bind(request)
    holder.btnRespond.setOnClickListener {
      val builder = AlertDialog.Builder(context)
      builder.setMessage(
        "Accept or reject friend request from " + String.format(
          context.getString(R.string.username),
          request.name,
        )
      ).setTitle("Respond to friend request")
        .setPositiveButton("Accept") { _, _ ->
          viewModel.acceptRequest(request.email)
        }
        .setNegativeButton("Reject") { _, _ ->
          viewModel.rejectRequest(request.email)
        }
        .create().show()
    }
  }

  override fun getItemCount(): Int = requests.size

  class ViewHolder(private val binding: RecyclerviewRequestIndividualOtherBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val btnRespond = binding.btnRespond

    fun bind(request: TingXieIndividual) = with(binding) {
      tvEmail.text = String.format(itemView.context.getString(R.string.braces), request.email)
      val name = request.name
      tvName.text = if (name.isEmpty()) "No Name" else String.format(itemView.context.getString(R.string.username), name)
    }
  }
}