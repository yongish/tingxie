package com.zhiyong.tingxie.ui.reading

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.databinding.FragmentReadingBinding
import com.zhiyong.tingxie.ui.readings.CharacterView
import com.zhiyong.tingxie.ui.readings.ReadingsAdapter.Companion.EXTRA_ID

class ReadingFragment : Fragment() {

  companion object {
    fun newInstance() = ReadingFragment()
  }

  private lateinit var viewModel: ReadingViewModel

  private var _binding: FragmentReadingBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentReadingBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val id = requireActivity().intent.getIntExtra(EXTRA_ID, -1)
    val viewModelFactory = ReadingViewModelFactory(requireActivity().application, id)
    val viewModel = ViewModelProvider(this, viewModelFactory)[ReadingViewModel::class.java]
    viewModel.passage.observe(viewLifecycleOwner) {
      it?.let {
        // Add the binding.reading views here.
      }
    }

//    var view = CharacterView(requireContext(), "hi")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "有")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "踏")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
//    view = CharacterView(requireContext(), "me")
//    binding.reading.addView(view)
  }
}