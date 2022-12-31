package com.zhiyong.tingxie.ui.group

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentGroupBinding
import com.zhiyong.tingxie.network.NetworkGroup
import com.zhiyong.tingxie.ui.group_membership.GroupMembershipViewModel
import com.zhiyong.tingxie.ui.group_membership.GroupMembershipViewModelFactory

class GroupFragment : Fragment() {

  companion object {
    fun newInstance() = GroupFragment()
  }

  private lateinit var viewModel: GroupMembershipViewModel
  private var _binding: FragmentGroupBinding? = null
  private val binding get() = _binding!!
  private lateinit var adapter: GroupAdapter

  private lateinit var email: String
  private lateinit var name: String

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    val currentUser = FirebaseAuth.getInstance().currentUser!!
    email = currentUser.email!!
    name = currentUser.displayName!!

    _binding = FragmentGroupBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.fab.setOnClickListener { openAddGroupDialog() }

    val viewModelFactory =
      GroupMembershipViewModelFactory(requireActivity().application, email)
    viewModel =
      ViewModelProvider(this, viewModelFactory)[GroupMembershipViewModel::class.java]
    adapter = GroupAdapter(requireActivity(), viewModel, binding.recyclerviewGroups)
    binding.recyclerviewGroups.adapter = adapter
    viewModel.groups.observe(viewLifecycleOwner) {
      it?.let {
        adapter.groups = it
        if (it.isEmpty()) {
          binding.emptyView.visibility = View.VISIBLE
        } else {
          binding.emptyView.visibility = View.INVISIBLE
        }
      }
    }

//    viewModel.groupsStatus.observe(viewLifecycleOwner) { status ->
//      if (status.equals(Status.ERROR)) {
//        binding.emptyView.visibility = View.INVISIBLE
//        binding.networkErrorView.visibility = View.VISIBLE
//      }
//    }
//    binding.swipeLayout.setOnRefreshListener {
//      viewModel.refreshRequests(binding.swipeLayout)
//    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun openAddGroupDialog() {
    val editText = EditText(context)
    val params = FrameLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
    params.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
    editText.layoutParams = params
    val frameLayout = context?.let { it1 ->
      FrameLayout(it1)
    }
    frameLayout?.addView(editText)
    val builder = AlertDialog.Builder(requireActivity())
    builder.setTitle("Create group")
      .setMessage("Create a new group.")
      .setView(frameLayout)
      .setPositiveButton(R.string.ok) { _, _ ->
        val name = editText.text.toString()
        viewModel.createGroup(name, listOf()).observe(viewLifecycleOwner) {
          val newId = it.toLongOrNull()
          if (newId == null) {
            Toast.makeText(
              context,
              "Error. Please contact yongish@gmail.com.",
              Toast.LENGTH_LONG
            ).show()
          } else {
            adapter.addNewGroup(NetworkGroup(newId, name, "OWNER", 1))
            binding.emptyView.visibility = View.INVISIBLE
          }
        }
      }
      .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
      .setNeutralButton("Share 听写") { _, _ ->
        context?.let {
          ShareCompat.IntentBuilder(it)
            .setType("text/plain")
            .setChooserTitle("Chooser title")
            .setText("http://play.google.com/store/apps/details?id=" + it.packageName)
            .startChooser()
        }
      }.create().show()
  }
}
