package com.zhiyong.tingxie.ui.readings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.databinding.FragmentReadingsBinding

class ReadingsFragment : Fragment() {

  companion object {
    fun newInstance() = ReadingsFragment()
  }

  private var _binding: FragmentReadingsBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: ReadingsViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentReadingsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel = ViewModelProvider(this)[ReadingsViewModel::class.java]
    val adapter =
      ReadingsAdapter(requireActivity(), viewModel, binding.recyclerviewReadings)
    viewModel.titles.observe(viewLifecycleOwner) {
      it?.let {
        adapter.readings = it
      }
    }
  }
}