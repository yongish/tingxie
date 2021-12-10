package com.zhiyong.tingxie.ui.friend

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.GroupMemberFragmentBinding
import com.zhiyong.tingxie.ui.friend.GroupNameAdapter.Companion.EXTRA_GROUP
import com.zhiyong.tingxie.ui.share.EnumQuizRole

class GroupMemberFragment : Fragment() {

  companion object {
    fun newInstance() = GroupMemberFragment()
  }

  private lateinit var viewModel: GroupMemberViewModel
  private var _binding: GroupMemberFragmentBinding? = null
  private val binding get() = _binding!!

  private var unaddedFriends: List<TingXieIndividual> = arrayListOf()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    _binding = GroupMemberFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val group = this.arguments?.getParcelable<TingXieGroup>(EXTRA_GROUP)
    if (group == null) {
      Toast.makeText(context, "Please email Zhiyong at yongish@gmail.com and share this hash #GROUPARGS", Toast.LENGTH_LONG).show()
      // todo: Log Crashlytics.
      return
    }

    val email = FirebaseAuth.getInstance().currentUser?.email
    if (email == null) {
      // todo: Log Crashlytics.
      Toast.makeText(context, "Please email Zhiyong at yongish@gmail.com and share this hash #GROUPMEMBER", Toast.LENGTH_LONG).show()
      return
    }

    val isAdmin = group.members.find { it.email == email }?.role == EnumQuizRole.EDITOR

    binding.fab.setOnClickListener {
      // stopped here. Show a list of names (email addresses) that have not been added.

      val builder = AlertDialog.Builder(requireActivity())
      if (isAdmin) {
        if (unaddedFriends.isEmpty()) {
          // Already added all your friends.
          // todo: Group tab should only be accessible if the user has > 0 friends.
          builder.setMessage(
              "You have already added all your friends.\nIf " +
              "you think this is a mistake, please email Zhiyong at yongish@gmail.com."
          )
              .setTitle("All friends added.")
              .setNegativeButton("Close") { dialog, _ -> dialog.dismiss() }
              .create().show()
        } else {
          val adapter = ArrayAdapter(
              requireContext(),
              android.R.layout.simple_spinner_item,
              unaddedFriends.map { it.firstName + it.lastName + " (" + it.email + ")" }
          )
          adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
          val spinner = Spinner(context)
          spinner.adapter = adapter

          val frameLayout = FrameLayout(requireContext())
          frameLayout.addView(spinner)

          // group admin can add and remove friends.
          builder.setMessage("Add a friend to your group.")
              .setTitle("Add to group")
              .setView(frameLayout)
              .setPositiveButton(R.string.ok) { _, _ ->
                val friend = unaddedFriends[spinner.selectedItemPosition]
                viewModel.add(
                    group.name,
                    TingXieGroupMember(
                        friend.email,
                        spinner.selectedItem as EnumQuizRole,
                        friend.firstName,
                        friend.lastName
                    )
                )
              }
              .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
              .create().show()
        }
      } else {
        // non-admin will see alert dialog to request to be an admin.
        builder.setMessage("You must be an administrator to add people to this group.")
            .setPositiveButton("Request administrator role") {
              // todo: Request admin role.
              dialog, _ -> dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create().show()
      }
    }

    viewModel = ViewModelProvider(this)[GroupMemberViewModel::class.java]
    viewModel.friends.observe(viewLifecycleOwner, { friends ->
      friends?.apply {
        unaddedFriends = friends - group.members.map { TingXieIndividual(it.email, it.firstName, it.lastName) }.toSet()
        binding.recyclerviewGroups.adapter = GroupMemberAdapter(
            group, requireContext(), viewModel, binding.recyclerviewGroups
        )
      }
    })

    if (group.members.isEmpty()) {
      binding.emptyView.visibility = View.VISIBLE
    } else {
      binding.emptyView.visibility = View.INVISIBLE
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
