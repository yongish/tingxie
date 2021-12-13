package com.zhiyong.tingxie.ui.friend.individual.requests.yours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.zhiyong.tingxie.databinding.YourIndividualRequestsFragmentBinding
import com.zhiyong.tingxie.viewmodel.Status

class YourRequestFragment : Fragment() {

  companion object {
    fun newInstance() = YourRequestFragment()
  }

  private lateinit var viewModel: YourViewModel
  private var _binding: YourIndividualRequestsFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    _binding = YourIndividualRequestsFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.requests.observe(viewLifecycleOwner, { request ->
      request?.apply {
//        binding.recyclerviewYourRequests.adapter =
      }
    })

    viewModel.status.observe(viewLifecycleOwner, { status ->
      if (status.equals(Status.ERROR)) {
        // todo: Display an offline error message on the view, instead of a toast.
        Toast.makeText(activity, "Network Error on Friends", Toast.LENGTH_LONG).show()
      }
    })
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}