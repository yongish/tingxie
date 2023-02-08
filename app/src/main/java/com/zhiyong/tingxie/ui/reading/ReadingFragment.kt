package com.zhiyong.tingxie.ui.reading

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.databinding.FragmentReadingBinding

class ReadingFragment : Fragment() {

  companion object {
    fun newInstance() = ReadingFragment()
  }

  private var _binding: FragmentReadingBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: ReadingViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this).get(ReadingViewModel::class.java)
    // TODO: Use the ViewModel
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentReadingBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//    var clCharacter = requireActivity().findViewById<ConstraintLayout>(R.id.clCharacter)
//    binding.reading.addView(clCharacter)

    var view = CharacterView(requireContext(), "hi")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)
    view = CharacterView(requireContext(), "me")
    binding.reading.addView(view)

  }
}