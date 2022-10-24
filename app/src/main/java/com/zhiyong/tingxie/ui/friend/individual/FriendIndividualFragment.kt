package com.zhiyong.tingxie.ui.friend.individual

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.databinding.IndividualFragmentBinding

class FriendIndividualFragment : Fragment() {

  companion object {
    fun newInstance() = FriendIndividualFragment()
  }

  private lateinit var viewModel: FriendIndividualViewModel
  private var _binding: IndividualFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    _binding = IndividualFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.swipeLayout.setOnRefreshListener {
      viewModel.refreshFriends(binding.swipeLayout)
    }

    viewModel = ViewModelProvider(this)[FriendIndividualViewModel::class.java]
    val adapter = FriendIndividualAdapter(
      requireActivity(), viewModel, binding.recyclerviewIndividuals
    )
    binding.recyclerviewIndividuals.adapter = adapter
    viewModel.friends.observe(viewLifecycleOwner) {
      it?.let {
        adapter.individuals = it
        if (it.isEmpty()) {
          binding.emptyView.visibility = View.VISIBLE
        } else {
          binding.emptyView.visibility = View.INVISIBLE
        }
      }
    }

//    viewModel.status.observe(viewLifecycleOwner) { status ->
//      if (status.equals(Status.ERROR)) {
//        // todo: Display an offline error message on the view, instead of a toast.
//        Toast.makeText(activity, "Network Error on FriendIndividualFragment", Toast.LENGTH_LONG).show()
//      }
//    }

    // what is this? may delete.
//    viewModel.shouldReopen.observe(viewLifecycleOwner) { status ->
//      if (status == true) {
//        openAddFriendDialog(true)
//      }
//    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}