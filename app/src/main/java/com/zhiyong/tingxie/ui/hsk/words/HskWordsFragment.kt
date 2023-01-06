package com.zhiyong.tingxie.ui.hsk.words

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.QuizRepository
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.TingXieApplication
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsFragment.Companion.EXTRA_LEVEL
import com.zhiyong.tingxie.ui.main.QuizViewModel
import com.zhiyong.tingxie.ui.main.QuizViewModelFactory
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller

class HskWordsFragment : Fragment() {

  companion object {
    fun newInstance() = HskWordsFragment()
  }

  private lateinit var viewModel: HskWordsViewModel
  private val quizViewModel by viewModels<QuizViewModel> {
    QuizViewModelFactory((requireContext().applicationContext as TingXieApplication).quizRepository)
  }
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

    viewModel = ViewModelProvider(this)[HskWordsViewModel::class.java]

    recyclerView = requireActivity().findViewById(R.id.recyclerview_hsk_words)
    val adapter = HskWordsAdapter(requireActivity(), viewModel, quizViewModel)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    adapter.setWordItems(viewModel.getHsk(level))

    // need to set quizzes too, because the adapter needs the quizzes for the add to quiz buttons.
    quizViewModel.allQuizItems.observe(requireActivity()) { adapter.setQuizItems(it) }

    val fastScroller: VerticalRecyclerViewFastScroller =
      requireActivity().findViewById(R.id.fast_scroller)
    fastScroller.setRecyclerView(recyclerView)
    recyclerView.addOnScrollListener(fastScroller.onScrollListener)
  }
}
