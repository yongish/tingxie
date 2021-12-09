package com.zhiyong.tingxie.ui.share

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.databinding.RecyclerviewShareBinding

import android.view.View.OnTouchListener
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import com.zhiyong.tingxie.R

enum class IsShared { SHARED, ALL }

class ShareAdapter(private val quizId: Long,
                   private val shares: List<TingXieShare>,
                   private val context: Context,
                   val viewModel: ShareViewModel,
                   val recyclerView: RecyclerView)
  : RecyclerView.Adapter<ShareAdapter.ViewHolder>(), Filterable {

  var sharesFiltered: List<TingXieShare> = shares.filter { it.isShared }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(RecyclerviewShareBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ))
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val share = sharesFiltered[position]
    holder.bind(share)

//    holder.ivDelete.setOnClickListener {
//      val adapterPosition = holder.adapterPosition
//      Snackbar
//          .make(recyclerView, "Removed ${share.firstName}", Snackbar.LENGTH_LONG)
//          .setAction("Undo") {
//            viewModel.addShare(share)
//            notifyItemInserted(adapterPosition)
//          }
//          .show()
//      viewModel.deleteShare(quizId, share.email)
//      notifyItemRemoved(adapterPosition)
//    }

    val adapter = ArrayAdapter(
        context,
        android.R.layout.simple_spinner_item,
        EnumQuizRole.values().map { role -> role.toString() }
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    holder.spRole.adapter = adapter
    holder.spRole.setSelection(EnumQuizRole.values().indexOf(share.role))

    holder.cbIsShared.isChecked = share.isShared

    val role = sharesFiltered
        .first { it.email == FirebaseAuth.getInstance().currentUser?.email }.role
    if (role == EnumQuizRole.VIEWER) {
      holder.spRole.alpha = .3f
      holder.spRole.setOnTouchListener(OnTouchListener
      @SuppressLint("ClickableViewAccessbility") { _, _ ->
        val builder = AlertDialog.Builder(context)

        // todo: Check if there is a pending editor request.

        builder.setMessage("You must be an editor to edit sharing settings.")
            .setPositiveButton("Request editor role") {
              // todo: Request editor role.
              dialog, _ -> dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create().show()
        true
      })
    } else {
//      // todo: Save menu item should appear.
//      holder.cbIsShared.setOnClickListener {
//
//      }
//      // todo: Save menu item should appear.
//      holder.spRole.setOnItemClickListener { adapterView, view, i, l ->  }
    }

  }

  override fun getItemCount(): Int = sharesFiltered.size

  override fun getFilter(): Filter {
    return object : Filter() {
      override fun performFiltering(constraint: CharSequence?): FilterResults {
        return FilterResults().apply {
          values = if (IsShared.valueOf(constraint.toString()) == IsShared.SHARED) {
            shares.filter { it.isShared }
          } else {
            shares
          }
        }
      }

      override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        sharesFiltered = results.values as List<TingXieShare>
        notifyDataSetChanged()
      }
    }
  }

  class ViewHolder(private val binding: RecyclerviewShareBinding)
    : RecyclerView.ViewHolder(binding.root) {
//    val ivDelete = binding.ivDelete
    val spRole = binding.spRole
    val cbIsShared = binding.cbIsShared

    fun bind(share: TingXieShare) = with(binding) {
      tvEmail.text = share.email
      tvName.text = String.format(
          itemView.context.getString(R.string.username),
          share.lastName,
          share.firstName,
      )
    }
  }
}
