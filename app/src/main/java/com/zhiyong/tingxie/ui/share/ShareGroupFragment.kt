package com.zhiyong.tingxie.ui.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.zhiyong.tingxie.databinding.ShareGroupFragmentBinding
import com.zhiyong.tingxie.ui.friend.GroupNameAdapter
import com.zhiyong.tingxie.ui.friend.Status
import com.zhiyong.tingxie.ui.share.ShareActivity.Companion.EXTRA_QUIZ_ID

class ShareGroupFragment: Fragment() {

  companion object {
    fun newInstance() = ShareGroupFragment()
  }

  private lateinit var viewModel: ShareGroupViewModel
  private var _binding: ShareGroupFragmentBinding? = null
  private val binding get() = _binding!!

  private lateinit var groupNames: List<String>

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    _binding = ShareGroupFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//    val isAdmin = group.

    binding.fab.setOnClickListener {
      val adapter = ArrayAdapter(
          requireContext(),
          android.R.layout.simple_spinner_item,
          groupNames,
      )
      val spinner = Spinner(context)
      spinner.adapter = adapter

      val frameLayout = FrameLayout(requireContext())
      frameLayout.addView(spinner)


    }

    val quizId = arguments?.getLong(EXTRA_QUIZ_ID)
    if (quizId == null) {
      // todo Log to Crashlytics.
    } else {
      viewModel = ViewModelProvider(this, ShareGroupViewModelFactory(requireActivity().application, quizId))[ShareGroupViewModel::class.java]
      viewModel.groups.observe(viewLifecycleOwner, { groups ->
        groups?.apply {
//          binding.recyclerviewShareGroups.adapter = GroupNameAdapter(
//              groups, requireActivity(), viewModel, binding.recyclerviewShareGroups
//          )
        }
        if (groups.isEmpty()) {
          binding.emptyView.visibility = View.VISIBLE
        } else {
          binding.emptyView.visibility = View.INVISIBLE
        }
      })
    }
  }

//  viewModel.status.observe(viewLifecycleOwner, { status ->
//    if (status.equals(Status.ERROR)) {
//      // todo: Display an offline error message on the view, instead of a toast.
//      Toast.makeText(activity, "Network Error on Groups", Toast.LENGTH_LONG).show()
//    }
//  })

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}