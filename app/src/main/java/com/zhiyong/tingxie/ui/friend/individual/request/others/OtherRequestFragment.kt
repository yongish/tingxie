package com.zhiyong.tingxie.ui.friend.individual.request.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.zhiyong.tingxie.databinding.OtherIndividualRequestFragmentBinding

class OtherRequestFragment : Fragment() {

  companion object {
    fun newInstance() = OtherRequestFragment()
  }

  private lateinit var viewModel: OtherViewModel
  private var _binding: OtherIndividualRequestFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    _binding = OtherIndividualRequestFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel = ViewModelProvider(this)[OtherViewModel::class.java]
    viewModel.requests.observe(viewLifecycleOwner, { requests ->
      requests?.apply {
        binding.recyclerviewOtherRequests.adapter = OtherRequestAdapter(
            requests, requireActivity(), viewModel, binding.recyclerviewOtherRequests
        )
      }
    })
  }
}
