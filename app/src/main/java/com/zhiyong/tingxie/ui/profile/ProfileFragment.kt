package com.zhiyong.tingxie.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentProfileBinding
import com.zhiyong.tingxie.network.NetworkProfile
import com.zhiyong.tingxie.ui.exercises_completed.ExercisesCompletedActivity


class ProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {

  companion object {
    fun newInstance() = ProfileFragment()
    const val EXTRA_GRADE_LEVEL = "com.zhiyong.tingxie.ui.profile.extra.GRADE_LEVEL"
    const val EXTRA_EMAIL = "com.zhiyong.tingxie.ui.profile.extra.EMAIL"
  }

  private var _binding: FragmentProfileBinding? = null
  private val binding get() = _binding!!

  private lateinit var email: String
  private lateinit var viewModel: ProfileViewModel
  private lateinit var profile: NetworkProfile

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val currentUser = FirebaseAuth.getInstance().currentUser!!
    email = currentUser.email!!

    _binding = FragmentProfileBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val viewModelFactory = ProfileViewModelFactory(requireActivity().application, email)
    viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    viewModel.profile.observe(viewLifecycleOwner) {
      it?.let {
        binding.spGradeLevel.setSelection(it.gradeLevel)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
          requireContext(),
          android.R.layout.simple_spinner_item,
          it.allGradeLevels.map { level -> level.toString() }.toTypedArray(),
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spGradeLevel.adapter = adapter

        binding.spRatio.setSelection(it.ratio)
        val ratioAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
          requireContext(),
          android.R.layout.simple_spinner_item,
          it.allRatios.map { ratio -> ratio.toString() }.toTypedArray(),
        )
        ratioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spRatio.adapter = ratioAdapter
      }
    }

    val intent = Intent(context, ExercisesCompletedActivity::class.java)
    val selectedItem = binding.spGradeLevel.selectedItem
    if (selectedItem != null) {
      intent.putExtra(EXTRA_GRADE_LEVEL, selectedItem.toString())
    }
    intent.putExtra(EXTRA_EMAIL, email)
    binding.btnViewExercises.setOnClickListener {
      requireContext().startActivity(intent)
    }
  }

  override fun onItemSelected(p0: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
    val item = p0.getItemAtPosition(p2).toString().toInt()
    if (p0.id == R.id.spGradeLevel) {
      profile.gradeLevel = item
    } else {
      profile.ratio = item
    }
    viewModel.putProfile(email, profile)
  }

  override fun onNothingSelected(p0: AdapterView<*>?) {
    // Nothing.
  }
}