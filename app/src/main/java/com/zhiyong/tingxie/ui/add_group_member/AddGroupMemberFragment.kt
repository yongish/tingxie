package com.zhiyong.tingxie.ui.add_group_member

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentAddGroupMemberBinding
import com.zhiyong.tingxie.network.NetworkGroup
import com.zhiyong.tingxie.ui.group.GroupActivity
import com.zhiyong.tingxie.ui.group_member.GroupMemberActivity

class AddGroupMemberFragment : Fragment() {

  companion object {
    fun newInstance() = AddGroupMemberFragment()
  }

  private var _binding: FragmentAddGroupMemberBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: AddGroupMemberViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this)[AddGroupMemberViewModel::class.java]
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAddGroupMemberBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val email = FirebaseAuth.getInstance().currentUser?.email
    if (email != null) {
      binding.tvEnterEmailSub.text = getString(R.string.your_email_address, email)
    }

    binding.clRole.setOnClickListener {
      AlertDialog.Builder(requireActivity()).setTitle("Roles").setMessage(
        """
1. Member - Can see who the members of the group are. Can remove oneself from the group. Cannot add or remove other members from the group. Also cannot change anyone's role.
2. Admin - Can add and remove people from the group, and change their roles. Cannot change the owner's role.
3. Owner (not an option here) - A group can only have 1 owner. The owner can transfer her ownership to another group member. The owner has all admin permissions, and can also delete the entire group."""
      ).setPositiveButton("Close") { dialog, _ -> dialog.cancel() }.create().show()
    }

    val networkGroup = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requireActivity().intent.getParcelableExtra(
        GroupActivity.EXTRA_NETWORK_GROUP, NetworkGroup::class.java
      )
    } else {
      requireActivity().intent.getParcelableExtra(GroupActivity.EXTRA_NETWORK_GROUP)
    }
    if (networkGroup == null) {
      binding.otherErrorView.visibility = View.VISIBLE
    }

    binding.btnSubmit.setOnClickListener {
      val etEmailString = binding.etEmail.text.toString()
      if (android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailString).matches()) {
        binding.tvEmailValid.visibility = View.INVISIBLE
        val groupId = networkGroup!!.id
        val role =
          if (binding.radioGroup.checkedRadioButtonId == R.id.rbMember) "MEMBER" else "ADMIN"
        viewModel.addMemberOrReturnNoUser(groupId, etEmailString, role)
          .observe(viewLifecycleOwner) {
            if (it.email.isEmpty()) {
              AlertDialog.Builder(requireActivity()).setTitle("Email not found")
                .setMessage("You entered \"$etEmailString\". Did you enter the correct email address? Tap on \"Share 听写\" to share this app.")
                .setPositiveButton("Close") { dialog, _ -> dialog.cancel() }
                .setNeutralButton("Share 听写") { _, _ ->
                  ShareCompat.IntentBuilder(requireActivity()).setType("text/plain")
                    .setChooserTitle("Chooser title")
                    .setText("http://play.google.com/store/apps/details?id=" + requireActivity().packageName)
                    .startChooser()
                }.create().show()
            } else {
              // todo: May be able to use just group ID instead of entire NetworkGroup object.
              val intent = Intent(context, GroupMemberActivity::class.java)
              intent.putExtra(GroupActivity.EXTRA_NETWORK_GROUP, networkGroup)
              startActivity(intent)
            }
          }
      } else {
        binding.tvEmailValid.visibility = View.VISIBLE
      }
    }
  }
}
