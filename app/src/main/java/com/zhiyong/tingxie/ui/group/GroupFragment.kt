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
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentGroupBinding
import com.zhiyong.tingxie.network.NetworkGroup

class GroupFragment : Fragment() {

  companion object {
    fun newInstance() = GroupFragment()
  }

  private lateinit var viewModel: GroupViewModel
  private var _binding: FragmentGroupBinding? = null
  private val binding get() = _binding!!
  private lateinit var adapter: GroupAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    _binding = FragmentGroupBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.fab.setOnClickListener { openAddGroupDialog() }

    viewModel = ViewModelProvider(this)[GroupViewModel::class.java]
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
          if (it.toLongOrNull() == null) {
            Toast.makeText(context, "Error. Please contact yongish@gmail.com.", Toast.LENGTH_LONG).show()
          } else {
            adapter.addNewGroup(NetworkGroup(it.toLong(), name, "OWNER", 1))
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


        // 12/4/22 refer to wordactivity to make this builder. should be simple.

//    builder.setMessage(
//      HtmlCompat.fromHtml(
//        (if (showError) "<p style=\"color:red\">No such email: ${email}</p>\n" +
//            "Please check that the email address is correct. " +
//            "If it is correct, please ask your friend to install 听写 and create an account.<br />" else "") +
//            "To connect with a friend, search for her email address." +
//            if (yourEmail == null) "" else "\n<br />Your email address: $yourEmail",
//        HtmlCompat.FROM_HTML_MODE_LEGACY
//      )
//    )
//      .setTitle("Add friend")
//      .setView(frameLayout)
//      .setPositiveButton(R.string.ok) { _, _ ->
//        email = editText.text.toString()
//        viewModel.checkIfShouldReopen(email)
//      }
//      .setNegativeButton(R.string.cancel) { dialog, _ ->
//        viewModel.closeAddFriendModal()
//        dialog.cancel()
//      }
//      .setNeutralButton("Share 听写") { _, _ ->
//        context?.let {
//          ShareCompat.IntentBuilder(it)
//            .setType("text/plain")
//            .setChooserTitle("Chooser title")
//            .setText("http://play.google.com/store/apps/details?id=" + it.packageName)
//            .startChooser()
//        }
//      }
//      .create().show()
  }
}
