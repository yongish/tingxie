package com.zhiyong.tingxie.ui.friend.group.name

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FriendGroupFragmentBinding
import com.zhiyong.tingxie.ui.friend.individual.Status

class FriendGroupNameFragment : Fragment() {

  companion object {
    fun newInstance() = FriendGroupNameFragment()
  }

  private lateinit var viewModel: FriendGroupNameViewModel
  private lateinit var groupNames: List<String>
  private var _binding: FriendGroupFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    _binding = FriendGroupFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.fab.setOnClickListener {
      val editText = EditText(context)
      val params = FrameLayout.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
      )
      params.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
      params.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
      editText.layoutParams = params
      val frameLayout = context?.let {
        it1 -> FrameLayout(it1)
      }

      frameLayout?.addView(editText)
      val builder = AlertDialog.Builder(requireActivity())
      builder.setMessage("Name your new group.\nExample: Class 5A")
          .setTitle("Create new group")
          .setView(frameLayout)
          .setPositiveButton(R.string.ok) { _, _ ->
            if (groupNames.contains(editText.text.toString())) {
              Toast.makeText(
                  context, "Group names must be unique", Toast.LENGTH_LONG
              ).show()
            } else {
              viewModel.addGroup(TingXieFriendGroup(editText.text.toString()))
            }
          }
          .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
          .create().show()
    }

    viewModel = ViewModelProvider(this)[FriendGroupNameViewModel::class.java]
    viewModel.groups.observe(viewLifecycleOwner, { groups ->
      groups?.apply {
        binding.recyclerviewGroups.adapter = FriendGroupNameAdapter(
            groups, requireActivity(), viewModel, binding.recyclerviewGroups
        )
        groupNames = groups.map { it.name }
      }
      if (groups.isEmpty()) {
        binding.emptyView.visibility = View.VISIBLE
      } else {
        binding.emptyView.visibility = View.INVISIBLE
      }
    })

    viewModel.status.observe(viewLifecycleOwner, { status ->
      if (status.equals(Status.ERROR)) {
        // todo: Display an offline error message on the view, instead of a toast.
        Toast.makeText(activity, "Network Error on Groups", Toast.LENGTH_LONG).show()
      }
    })
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
