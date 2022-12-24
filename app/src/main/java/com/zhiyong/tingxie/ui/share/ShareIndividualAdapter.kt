package com.zhiyong.tingxie.ui.share

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.databinding.RecyclerviewShareIndividualBinding

import android.view.View.OnTouchListener
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import com.zhiyong.tingxie.databinding.RecyclerviewGroupMemberBinding
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.ui.group_member.SelectRoleFragment

//enum class IsShared { SHARED, ALL }

class ShareIndividualAdapter(private val quizId: Long,
                             private val users: List<NetworkGroupMember>,
                             private val context: Context,
                             val viewModel: ShareIndividualViewModel,
                             val recyclerView: RecyclerView)
  : RecyclerView.Adapter<ShareIndividualAdapter.ViewHolder>() {

//  var sharesFiltered: List<TingXieShareIndividual> = shareIndividuals.filter { it.isShared }
  var editing = false

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      RecyclerviewGroupMemberBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ))
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val user = users[position]
    holder.bind(user)

    if (user.role != EnumQuizRole.OWNER.name) {
      holder.clIdentifier.setOnClickListener {
        val fm = (context as AppCompatActivity).supportFragmentManager
//        val selectRoleFragment: SelectRoleFragment =
//          SelectRoleFragment.newInstance(role == EnumQuizRole.OWNER, user, position)
//        selectRoleFragment.show(fm, "fragment_select_role")
      }
    }

//    holder.ivDelete.setOnClickListener {
//      val adapterPosition = holder.adapterPosition
//      Snackbar
//          .make(recyclerView, "Removed ${share.name}", Snackbar.LENGTH_LONG)
//          .setAction("Undo") {
//            viewModel.addShare(share)
//            notifyItemInserted(adapterPosition)
//          }
//          .show()
//      viewModel.deleteShare(quizId, share.email)
//      notifyItemRemoved(adapterPosition)
//    }

//    val adapter = ArrayAdapter(
//        context,
//        android.R.layout.simple_spinner_item,
//        EnumQuizRole.values().map { role -> role.toString() }
//    )
//    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//    holder.spRole.adapter = adapter
//    holder.spRole.setSelection(EnumQuizRole.values().indexOf(share.role))
//
//    holder.cbIsShared.isChecked = share.isShared
//    holder.cbIsShared.visibility = if (editing) View.VISIBLE else View.GONE
//
//    val role = sharesFiltered
//        .first { it.email == FirebaseAuth.getInstance().currentUser?.email }.role
//    if (role == EnumQuizRole.MEMBER) {
//      holder.spRole.alpha = .3f
//      holder.spRole.setOnTouchListener(OnTouchListener
//      @SuppressLint("ClickableViewAccessbility") { _, _ ->
//        val builder = AlertDialog.Builder(context)
//
//        // todo: Check if there is a pending editor request.
//
//        builder.setMessage("You must be an editor to edit sharing settings.")
//            .setPositiveButton("Request editor role") {
//              // todo: Request editor role.
//              dialog, _ -> dialog.dismiss()
//            }
//            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
//            .create().show()
//        true
//      })
//    } else {
////      // todo: Save menu item should appear.
////      holder.cbIsShared.setOnClickListener {
////
////      }
////      // todo: Save menu item should appear.
////      holder.spRole.setOnItemClickListener { adapterView, view, i, l ->  }
//    }

  }

  override fun getItemCount(): Int = users.size

//  override fun getFilter(): Filter = object : Filter() {
//    override fun performFiltering(constraint: CharSequence?): FilterResults =
//        FilterResults().apply {
//          values = if (IsShared.valueOf(constraint.toString()) == IsShared.SHARED) {
//            shareIndividuals.filter { it.isShared }
//          } else {
//            shareIndividuals
//          }
//        }
//
//    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
//      sharesFiltered = results.values as List<TingXieShareIndividual>
//      notifyDataSetChanged()
//    }
//  }

  class ViewHolder(private val binding: RecyclerviewGroupMemberBinding)
    : RecyclerView.ViewHolder(binding.root) {
    val clIdentifier = binding.clIdentifier
    val ivEditRole = binding.ivEditRole
    val ivDelete = binding.ivDelete

    fun bind(groupMember: NetworkGroupMember) = with(binding) {
      tvName.text = groupMember.userName
      tvEmail.text = groupMember.email
      tvRole.text = groupMember.role
    }
  }
}
