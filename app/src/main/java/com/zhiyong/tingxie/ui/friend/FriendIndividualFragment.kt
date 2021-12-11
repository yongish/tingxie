package com.zhiyong.tingxie.ui.friend

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
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.IndividualFragmentBinding

class FriendIndividualFragment : Fragment() {

  companion object {
    fun newInstance() = FriendIndividualFragment()
  }

  private lateinit var viewModel: FriendIndividualViewModel
  private var _binding: IndividualFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = IndividualFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.fab.setOnClickListener {
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
      builder.setMessage("To connect with a friend, search for her email address.${
        if (yourEmail == null) "" else "\nYour email address: $yourEmail"
      }")
              .setTitle("Add friend")
              .setView(frameLayout)
              .setPositiveButton(R.string.ok) { dialog, _ ->
                // todo Search for email after API is implemented.
              }
              .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
              .create().show()
    }

    viewModel = ViewModelProvider(this)[FriendIndividualViewModel::class.java]
    viewModel.friends.observe(viewLifecycleOwner, { friends ->
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
    })

    viewModel.status.observe(viewLifecycleOwner, { status ->
      if (status.equals(Status.ERROR)) {
        // todo: Display an offline error message on the view, instead of a toast.
        Toast.makeText(activity, "Network Error on Friends", Toast.LENGTH_LONG).show()
      }
    })
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}