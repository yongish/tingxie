package com.zhiyong.tingxie.ui.add_quiz_group

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.databinding.FragmentAddQuizGroupBinding
import com.zhiyong.tingxie.ui.EXTRA_USER_ROLE
import com.zhiyong.tingxie.ui.UserRole

class AddQuizGroupFragment : Fragment() {

  companion object {
    fun newInstance() = AddQuizGroupFragment()
  }

  private var _binding: FragmentAddQuizGroupBinding? = null
  private val binding get() = _binding!!

  private lateinit var adapter: AddQuizGroupAdapter

  private lateinit var viewModel: AddQuizGroupViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAddQuizGroupBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val email = FirebaseAuth.getInstance().currentUser?.email
    val userRole = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requireActivity().intent.getParcelableExtra(
        EXTRA_USER_ROLE, UserRole::class.java
      )
    } else {
      requireActivity().intent.getParcelableExtra(EXTRA_USER_ROLE)
    }
    if (userRole == null || email == null) {
      binding.otherErrorView.visibility = View.VISIBLE
    } else {
      viewModel = ViewModelProvider(this, AddQuizGroupViewModelFactory(requireActivity().application, email))[AddQuizGroupViewModel::class.java]
      adapter = AddQuizGroupAdapter(
        requireActivity(),
        viewModel,
        binding.recyclerviewAddQuizGroup,
        viewLifecycleOwner,
        userRole
      )
      binding.recyclerviewAddQuizGroup.adapter = adapter
      viewModel.groups.observe(viewLifecycleOwner) {
        it?.let {
          adapter.groups = it
          if (it.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
          } else {
            binding.emptyView.visibility = View.INVISIBLE
          }
        }
      }
    }
  }
}
