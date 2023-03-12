package com.zhiyong.tingxie.ui.group_member

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentSelectRoleBinding
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.ui.group_member.GroupMemberFragment.Companion.EXTRA_GROUP_MEMBER
import com.zhiyong.tingxie.ui.group_member.GroupMemberFragment.Companion.EXTRA_POSITION
import com.zhiyong.tingxie.ui.group_member.GroupMemberFragment.Companion.EXTRA_SHOW_OWNER_OPTION
import com.zhiyong.tingxie.ui.share.EnumQuizRole

class SelectRoleFragment : DialogFragment() {

  private var _binding: FragmentSelectRoleBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSelectRoleBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val groupMember =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arguments?.getParcelable(
        EXTRA_GROUP_MEMBER,
        NetworkGroupMember::class.java
      ) else arguments?.getParcelable(EXTRA_GROUP_MEMBER)
    val position = arguments?.getInt(EXTRA_POSITION)
    val showOwnerOption = arguments?.getBoolean(EXTRA_SHOW_OWNER_OPTION)

    if (groupMember?.role == "MEMBER") {
      binding.rbMember.isChecked = true
      binding.rbAdmin.isChecked = false
    } else {
      binding.rbAdmin.isChecked = true
      binding.rbMember.isChecked = false
    }

    Log.d("SelectRoleFragment onViewCreated: ", showOwnerOption.toString())
    if (showOwnerOption == true) binding.rbOwner.visibility = View.VISIBLE

    binding.clRole.setOnClickListener {
      AlertDialog.Builder(requireActivity()).setTitle("Role options").setMessage(
        """
1. Member - Can see who the members of the group are. Can remove oneself from the group. Cannot add or remove other members from the group. Also cannot change anyone's role. 
2. Admin - Can add and remove people from the group, and change their roles. Cannot change the owner's role.
3. Owner - This option is only visible if you are the group owner. Select this option to transfer your ownership to this user. You will become an admin.
        """
      ).setPositiveButton("Close") { dialog, _ -> dialog.cancel() }.create().show()
    }

    binding.btnOk.setOnClickListener {
      groupMember?.role = when (binding.radioGroup.checkedRadioButtonId) {
        R.id.rbMember -> "MEMBER"
        R.id.rbAdmin -> "ADMIN"
        else -> "OWNER"
      }
      setFragmentResult(
        REQUEST_KEY,
        bundleOf(EXTRA_GROUP_MEMBER to groupMember, EXTRA_POSITION to position)
      )
      dismiss()
    }

    binding.btnCancel.setOnClickListener {
      dismiss()
    }
  }

  companion object {
    const val REQUEST_KEY = "com.zhiyong.tingxie.ui.group_member.REQUEST_KEY"

    @JvmStatic
    fun newInstance(
      shouldShowOwner: () -> Boolean,
      groupMember: NetworkGroupMember,
      position: Int
    ) =
      SelectRoleFragment().apply {
        arguments = Bundle().apply {
          putBoolean(EXTRA_SHOW_OWNER_OPTION, shouldShowOwner())
          putParcelable(EXTRA_GROUP_MEMBER, groupMember)
          putInt(EXTRA_POSITION, position)
        }
      }
  }
}