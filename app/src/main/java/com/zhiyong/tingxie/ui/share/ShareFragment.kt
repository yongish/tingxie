package com.zhiyong.tingxie.ui.share

import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentShareBinding
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.ui.EXTRA_USER_ROLE
import com.zhiyong.tingxie.ui.UserRole
import com.zhiyong.tingxie.ui.group_member.GroupMemberFragment
import com.zhiyong.tingxie.ui.group_member.SelectRoleFragment.Companion.REQUEST_KEY
import com.zhiyong.tingxie.ui.select_quiz_member_type.SelectQuizMemberTypeActivity

class ShareFragment : Fragment() {

  companion object {
    @JvmStatic
    fun newInstance() = ShareFragment()

    const val EXTRA_EMAIL = "com.zhiyong.tingxie.ui.share.EXTRA_EMAIL"
  }

  private lateinit var adapter: ShareAdapter
  private lateinit var menuItem: MenuItem
  private var _binding: FragmentShareBinding? = null
  private val binding get() = _binding!!

  private lateinit var email: String

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val currentUser = FirebaseAuth.getInstance().currentUser!!
    email = currentUser.email!!

    _binding = FragmentShareBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    setHasOptionsMenu(true)
    super.onCreate(savedInstanceState)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val userRole = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requireActivity().intent.getParcelableExtra(
        EXTRA_USER_ROLE,
        UserRole::class.java
      )
    } else {
      requireActivity().intent.getParcelableExtra(EXTRA_USER_ROLE)
    }
    if (userRole == null) {
      binding.otherErrorView.visibility = View.VISIBLE
      binding.emptyView.visibility = View.INVISIBLE
      return
    }
    val quizId = userRole.id
    val role = userRole.role

    if (role == EnumQuizRole.ADMIN || role == EnumQuizRole.OWNER) {
      binding.fab.visibility = View.VISIBLE
      binding.fab.setOnClickListener {
        val intent = Intent(context, SelectQuizMemberTypeActivity::class.java)
        intent.putExtra(EXTRA_USER_ROLE, userRole)
        startActivity(intent)
      }
    } else {
      binding.fab.visibility = View.GONE
    }

//    menuItem.setOnMenuItemClickListener {
//      viewModel.setAllShared(quizId, it.isChecked)
//      true
//    }

    val viewModelFactory =
      ShareViewModelFactory(requireActivity().application, quizId)
    val viewModel =
      ViewModelProvider(this, viewModelFactory)[ShareViewModel::class.java]

    // todo: Pass role into ShareAdapter to fix duplication.
    adapter = ShareAdapter(
      requireActivity(),
      viewModel,
      binding.recyclerviewShares,
      viewLifecycleOwner,
      quizId,
      role
    )
    binding.recyclerviewShares.adapter = adapter

    viewModel.users.observe(viewLifecycleOwner) {
      adapter.users = it as MutableList<NetworkGroupMember>
      if (it.isEmpty()) {
        binding.emptyView.visibility = View.VISIBLE
      } else {
        binding.emptyView.visibility = View.INVISIBLE
      }
    }

    setFragmentResultListener(REQUEST_KEY) { _, bundle ->
      val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getParcelable(
          GroupMemberFragment.EXTRA_GROUP_MEMBER,
          NetworkGroupMember::class.java
        )
      } else {
        bundle.getParcelable(GroupMemberFragment.EXTRA_GROUP_MEMBER)
      }
      val position = bundle.getInt(GroupMemberFragment.EXTRA_POSITION)
      if (user != null) {
          viewModel.changeRole(userRole.id, user.email, user.role)
            .observe(viewLifecycleOwner) {
              if (it > 0) adapter.changeRole(user, position)
              if (role == EnumQuizRole.OWNER && user.role == EnumQuizRole.OWNER.name) {
                viewModel.changeRole(userRole.id, email, "ADMIN")
              }
            }
      }
    }

//      viewModel.status.observe(viewLifecycleOwner) { status: Status ->
//        if (status == Status.ERROR) {
//          // todo: Display an offline error message on the view, instead of a toast.
//          Toast.makeText(activity, "Network Error on Shares", Toast.LENGTH_LONG).show()
//        }
//      }

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
}
