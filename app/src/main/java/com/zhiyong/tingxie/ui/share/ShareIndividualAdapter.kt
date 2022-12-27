package com.zhiyong.tingxie.ui.share

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.zhiyong.tingxie.databinding.RecyclerviewGroupMemberBinding
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.ui.group_member.SelectRoleFragment

//enum class IsShared { SHARED, ALL }

class ShareIndividualAdapter(
  private val context: Context,
  val viewModel: ShareIndividualViewModel,
  val recyclerView: RecyclerView,
  private val viewLifecycleOwner: LifecycleOwner,
  private val quizId: Long,
  private val role: EnumQuizRole
) : RecyclerView.Adapter<ShareIndividualAdapter.ViewHolder>() {

  val user = FirebaseAuth.getInstance().currentUser
  val name = user?.displayName
  val email = user?.email
  //  var sharesFiltered: List<TingXieShareIndividual> = shareIndividuals.filter { it.isShared }
  var editing = false

  var users = mutableListOf<NetworkGroupMember>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      RecyclerviewGroupMemberBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      )
    )
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val user = users[position]
    holder.bind(user)

    if (user.role != EnumQuizRole.OWNER.name) {
      holder.clIdentifier.setOnClickListener {
        val fm = (context as AppCompatActivity).supportFragmentManager
        val selectRoleFragment: SelectRoleFragment =
          SelectRoleFragment.newInstance(
            user.role == EnumQuizRole.OWNER.name,
            user,
            position
          )
        selectRoleFragment.show(fm, "fragment_select_role")
      }
    }

    if (role == EnumQuizRole.MEMBER) {
      holder.ivEditRole.visibility = View.INVISIBLE
      holder.ivDelete.visibility = View.INVISIBLE
    } else {
      holder.ivDelete.setOnClickListener {
        if (user.email == email && user.role == EnumQuizRole.OWNER.name) {
          AlertDialog.Builder(context)
            .setTitle("Removal not allowed")
            .setMessage("You must appoint someone else as the quiz owner before removing yourself.")
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .create().show()
        } else {
          AlertDialog.Builder(context)
            .setTitle("Remove ${user.userName}?")
            .setMessage("Are you sure you want to remove ${user.userName} from this quiz?")
            .setPositiveButton("Yes") { dialog, _ ->
              viewModel.removeQuizMember(
                quizId,
                name,
                email,
                user.email
              ).observe(viewLifecycleOwner) {
                if (it.toInt() >= -1) {
                  // Backend returns -1 if there is no token found for user, but the user
                  // is removed anyway.
                  users.removeAt(position)
                  notifyItemChanged(position)
                }
              }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .create().show()
        }
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

  class ViewHolder(private val binding: RecyclerviewGroupMemberBinding) :
    RecyclerView.ViewHolder(binding.root) {
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
