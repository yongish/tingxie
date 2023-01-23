package com.zhiyong.tingxie.ui.exercise_type

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentExerciseTypeBinding

class ExerciseTypeFragment : Fragment() {

  companion object {
    fun newInstance() = ExerciseTypeFragment()
  }

  private var _binding: FragmentExerciseTypeBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: ExerciseTypeViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this)[ExerciseTypeViewModel::class.java]
    // TODO: Use the ViewModel
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.fragment_exercise_type, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val adapter = ExerciseTypeAdapter(requireActivity(), viewModel, binding.recyclerviewExerciseType)
    binding.recyclerviewExerciseType.adapter = adapter
    viewModel.
  }

}