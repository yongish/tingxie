package com.zhiyong.tingxie.ui.read_then_write

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhiyong.tingxie.databinding.FragmentReadThenWriteBinding

class ReadThenWriteFragment : Fragment() {

  companion object {
    fun newInstance() = ReadThenWriteFragment()
  }

  private var _binding: FragmentReadThenWriteBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: ReadThenWriteViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentReadThenWriteBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val layout = binding.readThenWrite

    // Create a simple textview first, then consider how to create a series of textviews.
    val textView = TextView(context)
    textView.text = "helloooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"
    layout.addView(textView)
  }
}