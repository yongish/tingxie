package com.zhiyong.tingxie.ui.exercises_completed

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentExercisesCompletedBinding
import com.zhiyong.tingxie.ui.profile.ProfileActivity
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

    // Should read from DB instead.
//    val email = requireActivity().intent.getStringExtra(EXTRA_EMAIL)
//    val gradeLevel = requireActivity().intent.getIntExtra(EXTRA_GRADE_LEVEL, 1)
    val currentUser = FirebaseAuth.getInstance().currentUser!!
    val email = currentUser.email!!

    val viewModelFactory =
      ExercisesCompletedViewModelFactory(requireActivity().application, email)
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

    val menuHost: MenuHost = requireActivity()
    menuHost.addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_exercises_completed, menu)
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        startActivity(Intent(context, ProfileActivity::class.java))
        return true
      }
    })
  }
}
