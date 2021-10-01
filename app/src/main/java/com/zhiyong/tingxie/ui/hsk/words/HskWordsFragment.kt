package com.zhiyong.tingxie.ui.hsk.words

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsFragment.Companion.EXTRA_LEVEL
import org.json.JSONArray

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

    var level = requireActivity().intent.getIntExtra(EXTRA_LEVEL, 0)
    val wordArray = JSONArray(requireActivity().assets
      .open("hsk-vocab-json/hsk-level-$level.json").bufferedReader().use{
        it.readText()
      }
    )
    val hskWords: MutableList<HskWordsAdapter.HskWord> = mutableListOf()
    for (i in 0 until wordArray.length()) {
      val word = wordArray.getJSONObject(i)
      val id = word.getInt("id")
      val hanzi = word.getString("hanzi")
      val pinyin = word.getString("pinyin")
      hskWords.add(HskWordsAdapter.HskWord(id, hanzi, pinyin))
    }

    recyclerView = requireActivity().findViewById(R.id.recyclerview_hsk_words)
    val adapter = HskWordsAdapter()
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    adapter.setWordItems(hskWords)

    viewModel = ViewModelProvider(this).get(HskWordsViewModel::class.java)
    // TODO: Use the ViewModel
  }
}