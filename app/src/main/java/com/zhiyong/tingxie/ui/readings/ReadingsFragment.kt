package com.zhiyong.tingxie.ui.readings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.databinding.FragmentReadingsBinding

class ReadingsFragment : Fragment() {

  companion object {
    fun newInstance() = ReadingsFragment()
  }

  private var _binding: FragmentReadingsBinding? = null
  private val binding get() = _binding!!

  private lateinit var email: String

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val currentUser = FirebaseAuth.getInstance().currentUser!!
    email = currentUser.email!!

    _binding = FragmentReadingsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val viewModelFactory = ReadingsViewModelFactory(requireActivity().application, email)
    val viewModel = ViewModelProvider(this, viewModelFactory)[ReadingsViewModel::class.java]
    val adapter =
      ReadingsAdapter(requireActivity(), viewModel, binding.recyclerviewReadings)
    binding.recyclerviewReadings.adapter = adapter
    viewModel.titles.observe(viewLifecycleOwner) {
      it?.let {
        adapter.readings = it
      }
    }
  }
}