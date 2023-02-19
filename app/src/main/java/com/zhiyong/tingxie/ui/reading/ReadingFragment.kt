package com.zhiyong.tingxie.ui.reading

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FragmentReadingBinding
import com.zhiyong.tingxie.ui.readings.ReadingsAdapter.Companion.EXTRA_ID

class ReadingFragment : Fragment() {

  companion object {
    fun newInstance() = ReadingFragment()
  }

  private lateinit var viewModel: ReadingViewModel

  private lateinit var wordList: List<CharacterView>

  private var _binding: FragmentReadingBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    _binding = FragmentReadingBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val id = requireActivity().intent.getLongExtra(EXTRA_ID, -1)
    val viewModelFactory = ReadingViewModelFactory(requireActivity().application, id)
    val viewModel =
      ViewModelProvider(this, viewModelFactory)[ReadingViewModel::class.java]
    viewModel.passage.observe(viewLifecycleOwner) {
      it?.let {
        // Add the binding.reading views here.

        // split passage into words. Which library to use here?
        // Each character has a CharacterView.
//        it.passage

      }
    }

    var view = CharacterView(requireContext(), "hi", 0)
//    view.groupIndex

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

    val menuHost: MenuHost = requireActivity()
    menuHost.addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_reading, menu)
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
          R.id.action_toggle_pinyin -> {
            wordList.forEach { view -> view.togglePinyin() }
            true
          }
          R.id.action_underline -> {

            true
          }
          else -> false
        }
      }
    })
  }
}