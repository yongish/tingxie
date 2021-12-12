package com.zhiyong.tingxie.ui.friend.individual

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import com.google.android.material.snackbar.Snackbar
import com.zhiyong.tingxie.R
import androidx.core.content.res.ResourcesCompat
import com.zhiyong.tingxie.databinding.RecyclerviewFriendIndividualBinding


class FriendIndividualAdapter(private val individuals: List<TingXieIndividual>,
                              private val context: Context,
                              val viewModel: FriendIndividualViewModel,
                              val recyclerView: RecyclerView)
  : RecyclerView.Adapter<FriendIndividualAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
      ViewHolder(RecyclerviewFriendIndividualBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      ))

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val spannableString = SpannableString(
      "To share a quiz with your friend, go to that quiz and tap on the @ icon."
    )
    val d: Drawable? = ResourcesCompat.getDrawable(
      context.resources, R.drawable.ic_baseline_share_black_24, null
    )
    d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
    spannableString.setSpan(d?.let {
      ImageSpan(it, ImageSpan.ALIGN_BOTTOM) },
      spannableString.toString().indexOf("@"),
     spannableString.toString().indexOf("@") + 1,
      Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )

    val individual = individuals[position]
    holder.bind(individual)
    holder.clIdentifier.setOnClickListener {
      val builder = AlertDialog.Builder(context)
      builder.setMessage(spannableString)
              .setNegativeButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
              .create().show()
    }
    holder.ivDelete.setOnClickListener {
      val adapterPosition = holder.adapterPosition
      Snackbar
        .make(recyclerView, "Removed ${individual.firstName}", Snackbar.LENGTH_LONG)
        .setAction("Undo") {
          viewModel.addIndividual(individual)
          notifyItemInserted(adapterPosition)
        }
        .show()
      viewModel.deleteIndividual(individual.email)
      notifyItemRemoved(adapterPosition)
    }
  }

  override fun getItemCount(): Int = individuals.size

  class ViewHolder(private val binding: RecyclerviewFriendIndividualBinding)
    : RecyclerView.ViewHolder(binding.root) {
    val clIdentifier = binding.clIdentifier
    val ivDelete = binding.ivDelete

    fun bind(individual: TingXieIndividual) = with(binding) {
      tvSub.text = individual.email
      tvName.text = String.format(
          itemView.context.getString(R.string.username),
          individual.lastName,
          individual.firstName,
      )
    }
  }
}
