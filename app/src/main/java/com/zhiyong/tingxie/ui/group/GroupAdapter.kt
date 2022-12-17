package com.zhiyong.tingxie.ui.group

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.RecyclerviewGroupBinding
import com.zhiyong.tingxie.network.NetworkGroup
import com.zhiyong.tingxie.ui.group.GroupActivity.Companion.EXTRA_NETWORK_GROUP
import com.zhiyong.tingxie.ui.group_member.GroupMemberActivity

class GroupAdapter(
  private val context: Context,
  val viewModel: GroupViewModel,
  val recyclerView: RecyclerView
) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

  var groups = mutableListOf<NetworkGroup>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(
      RecyclerviewGroupBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val spannableString = SpannableString(
      "To share a quiz with a group, go to that quiz and tap on the @ icon."
    )
    val d: Drawable? = ResourcesCompat.getDrawable(
      context.resources, R.drawable.ic_baseline_share_black_24, null
    )
    d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
    spannableString.setSpan(
      d?.let {
        ImageSpan(it, ImageSpan.ALIGN_BOTTOM)
      },
      spannableString.toString().indexOf("@"),
      spannableString.toString().indexOf("@") + 1,
      Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )

    val group = groups[position]
    holder.bind(group)
//    holder.bind(group.asDomainModel())
    holder.clIdentifier.setOnClickListener {
      val builder = AlertDialog.Builder(context)
      builder.setMessage(spannableString)
        .setNegativeButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
        .create().show()
    }

    holder.btnDetails.setOnClickListener {
      val intent = Intent(context, GroupMemberActivity::class.java)
      intent.putExtra(EXTRA_NETWORK_GROUP, group)
      context.startActivity(intent)
    }
  }

  override fun getItemCount(): Int = groups.size

  fun addNewGroup(networkGroup: NetworkGroup) {
    groups.add(networkGroup)
    notifyItemInserted(groups.size - 1)
  }

  class ViewHolder(private val binding: RecyclerviewGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val clIdentifier = binding.clIdentifier
    val btnDetails = binding.btnDetails

    fun bind(group: NetworkGroup) = with(binding) {
      tvName.text = group.name
      tvNumMembers.text = if (group.numMembers == 1) String.format(
        "%d member",
        group.numMembers
      ) else String.format("%d member", group.numMembers)
      tvRole.text = group.role
    }
  }
}
