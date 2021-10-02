package com.zhiyong.tingxie.ui.hsk.buttons

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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
    const val EXTRA_LEVEL = "com.zhiyong.tingxie.ui.hsk.words.LEVEL"
  }

  private lateinit var buttonsViewModel: HskButtonsViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.hsk_buttons_fragment, container, false)
  }

  fun onClickListenerHelper(level: Int) {
    val intent = Intent(activity, HskWordsActivity::class.java)
    intent.putExtra(EXTRA_LEVEL, level)
    startActivity(intent)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btnHsk1 = view.findViewById(R.id.btnHsk1)
    btnHsk1.setOnClickListener { onClickListenerHelper(1) }
    btnHsk2 = view.findViewById(R.id.btnHsk2)
    btnHsk2.setOnClickListener { onClickListenerHelper(2) }
    btnHsk3 = view.findViewById(R.id.btnHsk3)
    btnHsk3.setOnClickListener { onClickListenerHelper(3) }
    btnHsk4 = view.findViewById(R.id.btnHsk4)
    btnHsk4.setOnClickListener { onClickListenerHelper(4) }
    btnHsk5 = view.findViewById(R.id.btnHsk5)
    btnHsk5.setOnClickListener { onClickListenerHelper(5) }
    btnHsk6 = view.findViewById(R.id.btnHsk6)
    btnHsk6.setOnClickListener { onClickListenerHelper(6) }

    buttonsViewModel = ViewModelProvider(this).get(HskButtonsViewModel::class.java)
    // TODO: Use the ViewModel
  }
}
