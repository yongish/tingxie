package com.zhiyong.tingxie.ui.add_quiz_individual

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_ID
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentAddQuizIndividualBinding
import com.zhiyong.tingxie.ui.EXTRA_USER_ROLE
import com.zhiyong.tingxie.ui.UserRole
import com.zhiyong.tingxie.ui.share.ShareActivity

class AddQuizIndividualFragment : Fragment() {

  companion object {
    fun newInstance() = AddQuizIndividualFragment()
  }

  private var _binding: FragmentAddQuizIndividualBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: AddQuizIndividualViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this)[AddQuizIndividualViewModel::class.java]
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAddQuizIndividualBinding.inflate(inflater, container, false)
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
1. Member - Can see who can access this quiz. Can remove oneself from the quiz. Cannot add or remove other members from the quiz. Also cannot change anyone's role.
2. Admin - Can add and remove words and people to the quiz, and change peoples' roles. Cannot change the owner's role.
3. Owner (not an option here) - A quiz can only have 1 owner. The owner can transfer her ownership to another member. The owner has all admin permissions, and can also delete the entire quiz."""
      ).setPositiveButton("Close") { dialog, _ -> dialog.cancel() }.create().show()
    }

    val userRole = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requireActivity().intent.getParcelableExtra(
        EXTRA_USER_ROLE, UserRole::class.java
      )
    } else {
      requireActivity().intent.getParcelableExtra(EXTRA_USER_ROLE)
    }
    if (userRole == null) {
      binding.otherErrorView.visibility = View.VISIBLE
    }

    binding.btnSubmit.setOnClickListener {
      val etEmailString = binding.etEmail.text.toString()
      if (android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailString).matches()) {
        binding.tvEmailValid.visibility = View.INVISIBLE
        val groupId = userRole!!.id
        val role =
          if (binding.radioGroup.checkedRadioButtonId == R.id.rbMember) "MEMBER" else "ADMIN"

        viewModel.addQuizMemberOrReturnNoUser(groupId, etEmailString, role)
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
              val intent = Intent(context, ShareActivity::class.java)
              intent.putExtra(EXTRA_USER_ROLE, userRole)
              startActivity(intent)
            }
          }
      } else {
        binding.tvEmailValid.visibility = View.VISIBLE
      }
    }
  }
}
