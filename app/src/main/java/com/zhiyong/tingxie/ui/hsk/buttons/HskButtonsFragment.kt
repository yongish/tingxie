package com.zhiyong.tingxie.ui.hsk.buttons

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.word.WordActivity

class HskButtonsFragment : Fragment() {

  // todo: Maybe try an array of 5 buttons for the 5 HSK lists.
  private lateinit var btnHsk1: Button
  private lateinit var btnHsk2: Button

  companion object {
    fun newInstance() = HskButtonsFragment()
  }

  private lateinit var buttonsViewModel: HskButtonsViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.hsk_buttons_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btnHsk1 = view.findViewById(R.id.btnHsk1)
    btnHsk2 = view.findViewById(R.id.btnHsk2)

    btnHsk1.setOnClickListener {
//      startActivity(Intent(this@HskFragment.context, WordActivity::class.java))
      // todo: stopped here. Next is to create a HSK list (activity, fragment) pair.
      val intent = Intent(activity, WordActivity::class.java)
      intent.putExtra("level", "hsk1")
      activity?.startActivity(Intent(activity, WordActivity::class.java))
    }

    buttonsViewModel = ViewModelProvider(this).get(HskButtonsViewModel::class.java)
    // TODO: Use the ViewModel
  }
}
