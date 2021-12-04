package com.zhiyong.tingxie.ui.friend

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zhiyong.tingxie.databinding.RecyclerviewFriendBinding

import android.text.SpannableString
import android.text.style.ImageSpan
import com.zhiyong.tingxie.R
import android.text.Spannable
import androidx.core.content.res.ResourcesCompat


class IndividualAdapter(private val individuals: List<TingXieIndividual>,
                        private val context: Context,
                        val viewModel: FriendsViewModel,
                        val recyclerView: RecyclerView)
  : RecyclerView.Adapter<IndividualAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(RecyclerviewFriendBinding.inflate(
      LayoutInflater.from(parent.context), parent, false
    ))
  }

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

    val friend = individuals[position]
    holder.bind(friend)
    holder.llIdentifier.setOnClickListener {
      val builder = AlertDialog.Builder(context)
      builder.setMessage(spannableString)
              .setNegativeButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
              .create().show()
    }
    holder.ivDelete.setOnClickListener {
      val adapterPosition = holder.adapterPosition
      Snackbar
        .make(recyclerView, "Removed ${friend.firstName}", Snackbar.LENGTH_LONG)
        .setAction("Undo") {
          viewModel.addFriend(friend)
          notifyItemInserted(adapterPosition)
        }
        .show()
      viewModel.deleteFriend(friend.email)
      notifyItemRemoved(adapterPosition)
    }
  }

  override fun getItemCount(): Int = individuals.size

  class ViewHolder(private val binding: RecyclerviewFriendBinding)
    : RecyclerView.ViewHolder(binding.root) {
    val llIdentifier = binding.llIdentifier
    val ivDelete = binding.ivDelete

    fun bind(individual: TingXieIndividual) = with(binding) {
      tvEmail.text = individual.email
      tvName.text = String.format(
          itemView.context.getString(R.string.username),
          individual.lastName,
          individual.firstName,
      )
    }
  }
}
