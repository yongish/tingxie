package com.zhiyong.tingxie.ui.select_quiz_member_type

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.databinding.FragmentSelectQuizMemberTypeBinding

class SelectQuizMemberTypeFragment : Fragment() {

  companion object {
    fun newInstance() = SelectQuizMemberTypeFragment()
  }

  private lateinit var viewModel: SelectQuizMemberTypeViewModel

  private var _binding: FragmentSelectQuizMemberTypeBinding? = null
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this).get(SelectQuizMemberTypeViewModel::class.java)
    // TODO: Use the ViewModel
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSelectQuizMemberTypeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // stopped here
    binding.btnNext.setOnClickListener {  }
  }
}
