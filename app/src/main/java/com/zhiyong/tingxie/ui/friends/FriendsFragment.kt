package com.zhiyong.tingxie.ui.friends

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FriendsFragmentBinding

class FriendsFragment : Fragment() {

  companion object {
    fun newInstance() = FriendsFragment()
  }

  private lateinit var viewModel: FriendsViewModel
  private var _binding: FriendsFragmentBinding? = null
  private val binding get() = _binding!!

  private lateinit var recyclerView: RecyclerView

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FriendsFragmentBinding.inflate(inflater, container, false)
    return inflater.inflate(R.layout.friends_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel = ViewModelProvider(this).get(FriendsViewModel::class.java)
    viewModel.friends.observe(viewLifecycleOwner, { friends ->
      friends?.apply {

//        binding.recyclerviewFriends.adapter = FriendsAdapter(friends)
        val adapter = FriendsAdapter(friends, requireActivity())
//        binding.recyclerviewFriends.adapter = adapter
        recyclerView = requireActivity().findViewById(R.id.recyclerview_friends)
        recyclerView.adapter = adapter

        adapter.setEmailItems(friends)
//        adapter.setEmailItems(arrayListOf(TingXieFriend("hhhhhhhh")))

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