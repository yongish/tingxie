package com.zhiyong.tingxie.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.YourIndividualRequestFragmentBinding
import com.zhiyong.tingxie.viewmodel.Status

class GroupFragment : Fragment() {

  companion object {
    fun newInstance() = GroupFragment()
  }

  private lateinit var viewModel: GroupViewModel
  private var _binding: YourIndividualRequestFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    _binding = YourIndividualRequestFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.fab.setOnClickListener { openAddGroupDialog() }


    // stopped here

    binding.swipeLayout.setOnRefreshListener {
      viewModel.refreshRequests(binding.swipeLayout)
    }

    viewModel = ViewModelProvider(this)[GroupViewModel::class.java]
    val adapter = GroupAdapter(viewModel, binding.recyclerviewGroup)
    binding.recyclerviewGroup.adapter = adapter
    viewModel.groups.observe(viewLifecycleOwner) {
      it?.let {
        adapter.requests = it
        if (it.isEmpty()) {
          binding.emptyView.visibility = View.VISIBLE
        } else {
          binding.emptyView.visibility = View.INVISIBLE
        }
      }
    }

    viewModel.status.observe(viewLifecycleOwner) { status ->
      if (status.equals(Status.ERROR)) {
        // todo: Display an offline error message on the view, instead of a toast.
        Toast.makeText(
          activity,
          "Network Error on YourRequestFragment",
          Toast.LENGTH_LONG
        ).show()
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun openAddGroupDialog() {
    // todo: Show no internet connection message.

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
    val yourEmail = FirebaseAuth.getInstance().currentUser?.email
    val builder = AlertDialog.Builder(requireActivity())

    // 12/4/22 refer to wordactivity to make this builder. should be simple.

    builder.setMessage(
      HtmlCompat.fromHtml(
        (if (showError) "<p style=\"color:red\">No such email: ${email}</p>\n" +
            "Please check that the email address is correct. " +
            "If it is correct, please ask your friend to install 听写 and create an account.<br />" else "") +
            "To connect with a friend, search for her email address." +
            if (yourEmail == null) "" else "\n<br />Your email address: $yourEmail",
        HtmlCompat.FROM_HTML_MODE_LEGACY
      )
    )
      .setTitle("Add friend")
      .setView(frameLayout)
      .setPositiveButton(R.string.ok) { _, _ ->
        email = editText.text.toString()
        viewModel.checkIfShouldReopen(email)
      }
      .setNegativeButton(R.string.cancel) { dialog, _ ->
        viewModel.closeAddFriendModal()
        dialog.cancel()
      }
      .setNeutralButton("Share 听写") { _, _ ->
        context?.let {
          ShareCompat.IntentBuilder(it)
            .setType("text/plain")
            .setChooserTitle("Chooser title")
            .setText("http://play.google.com/store/apps/details?id=" + it.packageName)
            .startChooser()
        }
      }
      .create().show()
  }
}
