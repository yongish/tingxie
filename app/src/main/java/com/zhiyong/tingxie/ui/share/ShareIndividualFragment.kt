package com.zhiyong.tingxie.ui.share

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.setFragmentResultListener
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ShareIndividualFragmentBinding
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.ui.add_group_member.AddGroupMemberActivity
import com.zhiyong.tingxie.ui.group_member.GroupMemberFragment
import com.zhiyong.tingxie.ui.group_member.SelectRoleFragment.Companion.REQUEST_KEY
import com.zhiyong.tingxie.ui.share.ShareActivity.Companion.EXTRA_QUIZ_ID

class ShareIndividualFragment : Fragment() {

  private lateinit var viewModel: ShareIndividualViewModel
  private lateinit var shareIndividualAdapter: ShareIndividualAdapter
  private lateinit var menuItem: MenuItem
  private var _binding: ShareIndividualFragmentBinding? = null
  private val binding get() = _binding!!
  private var editing = false

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = ShareIndividualFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    setHasOptionsMenu(true)
    super.onCreate(savedInstanceState)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val quizId = requireActivity().intent.getLongExtra(EXTRA_QUIZ_ID, -1)

    val spannableString = SpannableString(resources.getString(R.string.no_shares))
    val d: Drawable? = ResourcesCompat.getDrawable(
      resources, R.drawable.ic_baseline_edit_white_24, null
    )
    d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
    spannableString.setSpan(
      d?.let { ImageSpan(it, ImageSpan.ALIGN_BOTTOM) },
      spannableString.toString().indexOf("+"),
      spannableString.toString().indexOf("+") + 1,
      Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )
    binding.emptyView.text = spannableString

//    menuItem.setOnMenuItemClickListener {
//      viewModel.setAllShared(quizId, it.isChecked)
//      true
//    }

    if (quizId != -1L) {
      val viewModelFactory =
        ShareIndividualViewModelFactory(requireActivity().application, quizId)
      viewModel =
        ViewModelProvider(this, viewModelFactory)[ShareIndividualViewModel::class.java]

      viewModel.users.observe(viewLifecycleOwner) { users ->
        if (users.isEmpty()) {
          binding.emptyView.visibility = View.VISIBLE
        } else {
          binding.emptyView.visibility = View.INVISIBLE
        }

        users?.apply {
          if (users.isNotEmpty()) {
            val role =
              users.first { it.email == FirebaseAuth.getInstance().currentUser?.email }.role
            if (role == EnumQuizRole.ADMIN.name || role == EnumQuizRole.OWNER.name) {
              binding.fab.visibility = View.VISIBLE
              binding.fab.setOnClickListener {
                val intent = Intent(context, AddGroupMemberActivity::class.java)
//                intent.putExtra(EXTRA_NETWORK_GROUP, user)
              }
            } else {
              binding.fab.visibility = View.GONE
            }
          }


          // todo: Pass role into ShareAdapter to fix duplication.
          binding.recyclerviewShares.adapter = ShareIndividualAdapter(
            quizId,
            users,
            requireActivity(),
            viewModel,
            binding.recyclerviewShares,
          )

          shareIndividualAdapter =
            binding.recyclerviewShares.adapter as ShareIndividualAdapter
        }
      }

      setFragmentResultListener(REQUEST_KEY) { _, bundle ->
        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          bundle.getParcelable(GroupMemberFragment.EXTRA_GROUP_MEMBER, NetworkGroupMember::class.java)
        } else {
          bundle.getParcelable(GroupMemberFragment.EXTRA_GROUP_MEMBER)
        }
        val position = bundle.getInt(GroupMemberFragment.EXTRA_POSITION)
        if (user != null) {
//          viewModel.changeRole(networkGroup?.id, groupMember.email, groupMember.role)
//            .observe(viewLifecycleOwner) {
//              if (it > 0) adapter.changeRole(groupMember, position)
//              if (role == EnumQuizRole.OWNER && groupMember.role == EnumQuizRole.OWNER.name) {
//                viewModel.changeRole(networkGroup?.id, email, "ADMIN")
//              }
//            }
        }
      }

//      viewModel.status.observe(viewLifecycleOwner) { status: Status ->
//        if (status == Status.ERROR) {
//          // todo: Display an offline error message on the view, instead of a toast.
//          Toast.makeText(activity, "Network Error on Shares", Toast.LENGTH_LONG).show()
//        }
//      }

    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//    inflater.inflate(R.menu.menu_share, menu)
    menuItem = menu.add("Item1")
    menuItem.setActionView(R.layout.action_layout_checkbox)
      .setVisible(false)
      .setCheckable(true)
      .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    @JvmStatic
    fun newInstance(quizId: Long) = ShareIndividualFragment().apply {
      arguments = Bundle().apply {
        putLong(EXTRA_QUIZ_ID, quizId)
      }
    }
  }
}
