package com.zhiyong.tingxie.ui.share

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zhiyong.tingxie.databinding.ShareFragmentBinding
import com.zhiyong.tingxie.ui.friends.Status
import com.zhiyong.tingxie.ui.share.ShareActivity.Companion.EXTRA_QUIZ_ID

class ShareFragment : Fragment() {

  companion object {
    fun newInstance() = ShareFragment()
  }

  private lateinit var viewModel: ShareViewModel
  private var _binding: ShareFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    _binding = ShareFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val quizId = arguments?.getLong(EXTRA_QUIZ_ID)

    if (quizId != null) {
      val viewModelFactory = ShareViewModelFactory(quizId, requireNotNull(activity).application)
      viewModel = ViewModelProvider(this, viewModelFactory).get(ShareViewModel::class.java)

      viewModel.shares.observe(viewLifecycleOwner, { shares ->
        shares?.apply {
          binding.recyclerviewShares.adapter = ShareAdapter(
              quizId, shares, requireActivity(), viewModel, binding.recyclerviewShares
          )
        }
      })

      viewModel.status.observe(viewLifecycleOwner, { status ->
        if (status.equals(Status.ERROR)) {
          // todo: Display an offline error message on the view, instead of a toast.
          Toast.makeText(activity, "Network Error on Friends", Toast.LENGTH_LONG).show()
        }
      })
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
