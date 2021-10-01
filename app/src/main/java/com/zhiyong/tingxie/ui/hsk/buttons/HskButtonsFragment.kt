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
import com.zhiyong.tingxie.ui.hsk.words.HskWordsActivity

class HskButtonsFragment : Fragment() {

  private lateinit var btnHsk1: Button
  private lateinit var btnHsk2: Button
  private lateinit var btnHsk3: Button
  private lateinit var btnHsk4: Button
  private lateinit var btnHsk5: Button
  private lateinit var btnHsk6: Button

  companion object {
    fun newInstance() = HskButtonsFragment()
    val EXTRA_LEVEL = "com.zhiyong.tingxie.ui.hsk.words.LEVEL"
  }

  private lateinit var buttonsViewModel: HskButtonsViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.hsk_buttons_fragment, container, false)
  }

  fun setOnClickListenerHelper() {

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btnHsk1 = view.findViewById(R.id.btnHsk1)
    btnHsk2 = view.findViewById(R.id.btnHsk2)

    btnHsk1.setOnClickListener {
      val intent = Intent(activity, HskWordsActivity::class.java)
      intent.putExtra(EXTRA_LEVEL, 1)
      startActivity(intent)
    }

    buttonsViewModel = ViewModelProvider(this).get(HskButtonsViewModel::class.java)
    // TODO: Use the ViewModel
  }
}
