package com.zhiyong.tingxie.ui.exercises_completed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.databinding.FragmentExercisesCompletedBinding
import com.zhiyong.tingxie.ui.profile.ProfileFragment.Companion.EXTRA_EMAIL
import com.zhiyong.tingxie.ui.profile.ProfileFragment.Companion.EXTRA_GRADE_LEVEL

class ExercisesCompletedFragment : Fragment() {

  companion object {
    fun newInstance() = ExercisesCompletedFragment()
  }

  private var _binding: FragmentExercisesCompletedBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentExercisesCompletedBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val email = requireActivity().intent.getStringExtra(EXTRA_EMAIL)
    val gradeLevel = requireActivity().intent.getIntExtra(EXTRA_GRADE_LEVEL, 1)

    val viewModelFactory = ExercisesCompletedViewModelFactory(
      requireActivity().application,
      gradeLevel,
      email ?: ""
    )
    val viewModel =
      ViewModelProvider(this, viewModelFactory)[ExercisesCompletedViewModel::class.java]
    val adapter = ExercisesCompletedAdapter(
      requireActivity(),
      viewModel,
      binding.recyclerviewExerciseType
    )
    binding.recyclerviewExerciseType.adapter = adapter
    viewModel.exerciseTypes.observe(viewLifecycleOwner) {
      it?.let {
        adapter.exerciseTypes = it
      }
    }
  }
}
