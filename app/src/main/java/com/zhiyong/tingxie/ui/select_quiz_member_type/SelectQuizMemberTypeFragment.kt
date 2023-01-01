package com.zhiyong.tingxie.ui.select_quiz_member_type

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentSelectQuizMemberTypeBinding
import com.zhiyong.tingxie.ui.EXTRA_USER_ROLE
import com.zhiyong.tingxie.ui.UserRole
import com.zhiyong.tingxie.ui.add_quiz_group.AddQuizGroupActivity
import com.zhiyong.tingxie.ui.add_quiz_individual.AddQuizIndividualActivity
import com.zhiyong.tingxie.ui.share.ShareFragment.Companion.EXTRA_EMAIL

class SelectQuizMemberTypeFragment : Fragment() {

  companion object {
    fun newInstance() = SelectQuizMemberTypeFragment()
  }

  private var _binding: FragmentSelectQuizMemberTypeBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSelectQuizMemberTypeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
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
    binding.btnNext.setOnClickListener {
      val intent = Intent(context,
        if (binding.radioGroup.checkedRadioButtonId == R.id.rbGroup) AddQuizGroupActivity::class.java else AddQuizIndividualActivity::class.java
      )
      intent.putExtra(EXTRA_USER_ROLE, userRole)
      startActivity(intent)
    }
  }
}
