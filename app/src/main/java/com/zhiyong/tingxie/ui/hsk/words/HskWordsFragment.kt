package com.zhiyong.tingxie.ui.hsk.words

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsFragment.Companion.EXTRA_LEVEL
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller

class HskWordsFragment : Fragment() {

  companion object {
    fun newInstance() = HskWordsFragment()
  }

  private lateinit var viewModel: HskWordsViewModel
  private lateinit var recyclerView: RecyclerView

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.hsk_words_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val level = requireActivity().intent.getIntExtra(EXTRA_LEVEL, 0)

    viewModel = ViewModelProvider(this).get(HskWordsViewModel::class.java)

    recyclerView = requireActivity().findViewById(R.id.recyclerview_hsk_words)
    val adapter = HskWordsAdapter(requireContext())
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    adapter.setWordItems(viewModel.getHsk(level))

    val fastScroller: VerticalRecyclerViewFastScroller = requireActivity().findViewById(R.id.fast_scroller)
    fastScroller.setRecyclerView(recyclerView);
    recyclerView.addOnScrollListener(fastScroller.onScrollListener)
  }
}
