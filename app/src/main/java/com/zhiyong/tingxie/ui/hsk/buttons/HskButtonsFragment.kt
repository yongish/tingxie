package com.zhiyong.tingxie.ui.hsk.buttons

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.hsk.words.HskWordsActivity
import com.zhiyong.tingxie.ui.question.QuestionActivity

class HskButtonsFragment : Fragment() {

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

  private fun assignBtnsView(view: View): (Array<Int>, Class<*>) -> Unit {
    return fun (btnIdArray: Array<Int>, clazz: Class<*>) {
      btnIdArray.forEachIndexed { i, id -> run {
        val btn: Button = view.findViewById(id)
        btn.setOnClickListener {
          val intent = Intent(activity, clazz)
          intent.putExtra(EXTRA_LEVEL, i + 1)
          startActivity(intent)
        }
      } }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val assignBtns = assignBtnsView(view)
    assignBtns(arrayOf(
      R.id.btnHsk1, R.id.btnHsk2, R.id.btnHsk3, R.id.btnHsk4, R.id.btnHsk5, R.id.btnHsk6
    ), HskWordsActivity::class.java)
    assignBtns(arrayOf(
      R.id.btnStartHsk1, R.id.btnStartHsk2, R.id.btnStartHsk3,
      R.id.btnStartHsk4, R.id.btnStartHsk5, R.id.btnStartHsk6
    ), QuestionActivity::class.java)

    buttonsViewModel = ViewModelProvider(this).get(HskButtonsViewModel::class.java)
    // TODO: Use the ViewModel
  }
}
