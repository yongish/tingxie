package com.zhiyong.tingxie.ui.friend.individual

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.core.text.HtmlCompat
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.IndividualFragmentBinding
import com.zhiyong.tingxie.viewmodel.Status

class FriendIndividualFragment : Fragment() {

  companion object {
    fun newInstance() = FriendIndividualFragment()
  }

  private lateinit var viewModel: FriendIndividualViewModel
  private var _binding: IndividualFragmentBinding? = null
  private val binding get() = _binding!!

  private var email: String = ""

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    _binding = IndividualFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  private fun openAddFriendDialog(showError: Boolean = false) {
    // todo: Show no internet connection message.

    val editText = EditText(context)
    val params = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    params.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
    params.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
    editText.layoutParams = params
    val frameLayout = context?.let {
      it1 -> FrameLayout(it1)
    }
    frameLayout?.addView(editText)
    val yourEmail = FirebaseAuth.getInstance().currentUser?.email
    val builder = AlertDialog.Builder(requireActivity())
    builder.setMessage(HtmlCompat.fromHtml(
        (if (showError) "<p style=\"color:red\">No such email: ${email}</p>\n" +
            "Please check that the email address is correct. " +
            "If it is correct, please ask your friend to install 听写 and create an account.\n" else "") +
        "To connect with a friend, search for her email address." +
            if (yourEmail == null) "" else "\nYour email address: $yourEmail",
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ))
        .setTitle("Add friend")
        .setView(frameLayout)
        .setPositiveButton(R.string.ok) { _, _ ->
          email = editText.text.toString()
//          viewModel.checkIfShouldReopen(email)
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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel = ViewModelProvider(this)[FriendIndividualViewModel::class.java]

    binding.fab.setOnClickListener { openAddFriendDialog() }

    viewModel.friends.observe(viewLifecycleOwner) { friends ->
      friends?.apply {
        binding.recyclerviewIndividuals.adapter = FriendIndividualAdapter(
            friends, requireActivity(), viewModel, binding.recyclerviewIndividuals
        )
      }
      if (friends.isEmpty()) {
        binding.emptyView.visibility = View.VISIBLE
      } else {
        binding.emptyView.visibility = View.INVISIBLE
      }
    }

    viewModel.status.observe(viewLifecycleOwner) { status ->
      if (status.equals(Status.ERROR)) {
        // todo: Display an offline error message on the view, instead of a toast.
        Toast.makeText(activity, "Network Error on Friends", Toast.LENGTH_LONG).show()
      }
    }

    viewModel.shouldReopen.observe(viewLifecycleOwner) { status ->
      if (status == true) {
        openAddFriendDialog(true)
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}