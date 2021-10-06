package com.zhiyong.tingxie.ui.question

import android.os.Bundle
import android.os.PersistableBundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsFragment.Companion.EXTRA_LEVEL
import com.zhiyong.tingxie.ui.hsk.words.HskQuestionItem
import com.zhiyong.tingxie.ui.hsk.words.HskWordsViewModel

class HskQuestionActivity: AppCompatActivity() {
  private lateinit var questionViewModel: HskWordsViewModel

  private lateinit var tvQuestionPinyin: TextView
  private lateinit var ivPlay: ImageView
  private lateinit var textToSpeech: TextToSpeech
  private lateinit var btnShowAnswer: Button
  private lateinit var btnErase: Button
  private lateinit var myCanvasView: MyCanvasView

  override fun onCreate(
    savedInstanceState: Bundle?,
    persistentState: PersistableBundle?
  ) {
    super.onCreate(savedInstanceState, persistentState)
    setContentView(R.layout.activity_question)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    // From HskButtonsActivity or HskAnswerActivity.
    val level = intent.getIntExtra(EXTRA_LEVEL, 1)

    // todo: findViewById for various elements.
    tvQuestionPinyin = findViewById()

    questionViewModel = ViewModelProvider(this).get(HskWordsViewModel::class.java)
    questionViewModel.getUnaskedHskQuestions(1).observe(this, { questions ->
      val question: HskQuestionItem = questions[0]
      question.
    })
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }
}
