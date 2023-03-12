package com.zhiyong.tingxie.ui.add_quiz_group

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.databinding.RecyclerviewAddQuizGroupBinding
import com.zhiyong.tingxie.network.NetworkGroup
import com.zhiyong.tingxie.ui.EXTRA_USER_ROLE
import com.zhiyong.tingxie.ui.UserRole
import com.zhiyong.tingxie.ui.group_member.GroupMemberActivity
import com.zhiyong.tingxie.ui.share.EnumQuizRole
import com.zhiyong.tingxie.ui.share.ShareActivity

class AddQuizGroupAdapter(
  private val context: Context,
  val viewModel: AddQuizGroupViewModel,
  val recyclerView: RecyclerView,
  private val viewLifecycleOwner: LifecycleOwner,
  private val userRole: UserRole
) :
  RecyclerView.Adapter<AddQuizGroupAdapter.ViewHolder>() {

  var groups = mutableListOf<NetworkGroup>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(
      RecyclerviewAddQuizGroupBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val group = groups[position]
    holder.bind(group)
    holder.btnDetails.setOnClickListener {
      val intent = Intent(context, GroupMemberActivity::class.java)
      intent.putExtra(
        EXTRA_USER_ROLE,
        UserRole(group.id, EnumQuizRole.valueOf(group.role))
      )
      context.startActivity(intent)
    }
    holder.btnChooseGroup.setOnClickListener {
      viewModel.addGroupMembersToQuiz(userRole.id, group.id).observe(viewLifecycleOwner) {
        try {
          if (Integer.valueOf(it) < 0) {
            Toast.makeText(
              context,
              "Group was not added to quiz. Contact yongish@gmail.com if you think this was an error.",
              Toast.LENGTH_LONG
            ).show()
          }
        } catch (e: Exception) {
          Toast.makeText(
            context,
            "Group was not added to quiz. Contact yongish@gmail.com if you think this was an error.",
            Toast.LENGTH_LONG
          ).show()
        }
        val intent = Intent(context, ShareActivity::class.java)
        intent.putExtra(EXTRA_USER_ROLE, userRole)
        context.startActivity(intent)
      }
    }
  }

  override fun getItemCount(): Int = groups.size

  class ViewHolder(private val binding: RecyclerviewAddQuizGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val btnDetails = binding.btnMembers
    val btnChooseGroup = binding.btnChooseGroup

    fun bind(group: NetworkGroup) = with(binding) {
      tvName.text = group.name
      tvNumMembers.text = if (group.numMembers == 1) String.format(
        "%d member",
        group.numMembers
      ) else String.format("%d members", group.numMembers)
      tvRole.text = group.role
    }
  }
}
