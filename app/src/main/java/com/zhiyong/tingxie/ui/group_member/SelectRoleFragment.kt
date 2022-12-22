package com.zhiyong.tingxie.ui.group_member

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentSelectRoleBinding
import com.zhiyong.tingxie.network.NetworkGroupMember
import com.zhiyong.tingxie.ui.group_member.GroupMemberFragment.Companion.EXTRA_GROUP_MEMBER
import com.zhiyong.tingxie.ui.group_member.GroupMemberFragment.Companion.EXTRA_POSITION

/**
 * A simple [Fragment] subclass.
 * Use the [SelectRoleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

    if (groupMember?.role == "MEMBER") {
      binding.rbMember.isChecked = true
      binding.rbAdmin.isChecked = false
    } else {
      binding.rbAdmin.isChecked = true
      binding.rbMember.isChecked = false
    }

    binding.btnOk.setOnClickListener {
      groupMember?.role = if (binding.radioGroup.checkedRadioButtonId == R.id.rbMember)
        "MEMBER" else "ADMIN"
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectRoleFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(groupMember: NetworkGroupMember, position: Int) =
      SelectRoleFragment().apply {
        arguments = Bundle().apply {
          putParcelable(EXTRA_GROUP_MEMBER, groupMember)
          putInt(EXTRA_POSITION, position)
        }
      }
  }
}