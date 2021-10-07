package com.zhiyong.tingxie.ui.question

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.answer.HskAnswerActivity
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsFragment.Companion.EXTRA_LEVEL
import com.zhiyong.tingxie.ui.hsk.words.HskWordsViewModel
import com.zhiyong.tingxie.ui.main.MainActivity
import com.zhiyong.tingxie.ui.question.QuestionActivity.*
import java.util.*
import kotlin.text.StringBuilder

class HskQuestionActivity: AppCompatActivity() {
  private lateinit var questionViewModel: HskWordsViewModel

  private lateinit var btnShowAnswer: Button
  private lateinit var btnErase: Button
  private lateinit var btnReset: Button
  private lateinit var ivPlay: ImageView
  private lateinit var myCanvasView: MyCanvasView
  private lateinit var textToSpeech: TextToSpeech
  private lateinit var tvQuestionPinyin: TextView
  private lateinit var tvRemaining: TextView

  private fun speak(hanzi: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      textToSpeech.speak(hanzi, TextToSpeech.QUEUE_FLUSH,null,null)
    } else {
      textToSpeech.speak(hanzi, TextToSpeech.QUEUE_FLUSH, null)
    }
  }

  override fun onCreate(
    savedInstanceState: Bundle?
  ) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_question)

    val toolbar: Toolbar = findViewById(R.id.toolbar)
    setSupportActionBar(toolbar)
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    toolbar.setNavigationOnClickListener {
      startActivity(Intent(this, MainActivity::class.java))
    }

    btnShowAnswer = findViewById(R.id.btnShowAnswer)
    btnErase = findViewById(R.id.btnErase)
    btnReset = findViewById(R.id.btnReset)
    ivPlay = findViewById(R.id.ivPlay)
    myCanvasView = findViewById(R.id.view)
    tvQuestionPinyin = findViewById(R.id.tvQuestionPinyin)
    tvRemaining = findViewById(R.id.tvRemaining)

    questionViewModel = ViewModelProvider(this).get(HskWordsViewModel::class.java)

    val level = intent.getIntExtra(EXTRA_LEVEL, -2)
    val unaskedWords = questionViewModel.getUnaskedHskWords(level)
    val numWordsInLevel = questionViewModel.getHsk(level).size
    tvRemaining.text = this.getString(
      R.string.remaining, unaskedWords.size, numWordsInLevel
    )

    val unaskedWord = unaskedWords.random()
    tvQuestionPinyin.text = unaskedWord.pinyin

      textToSpeech = TextToSpeech(applicationContext) { status ->
        if (status != TextToSpeech.ERROR) {
          //if there is no error then set language
          textToSpeech.language = Locale.SIMPLIFIED_CHINESE
          speak(unaskedWord.hanzi)
        }
      }

    ivPlay.setOnClickListener {
      speak(unaskedWord.hanzi)
    }

    btnErase.setOnClickListener { myCanvasView.erase() }

    btnReset.setOnClickListener {
      val isReset = questionViewModel.resetAsked(level)
      if (isReset) {
        tvRemaining.text = this.getString(
          R.string.remaining, numWordsInLevel, numWordsInLevel
        )
      }
    }

    btnShowAnswer.setOnClickListener {
      val hanzisMatchingPinyin = questionViewModel.getHanzis(level, unaskedWord.pinyin)

      val intent = Intent(applicationContext, HskAnswerActivity::class.java)
      val sb = StringBuilder()
      hanzisMatchingPinyin.forEach { sb.append("\n$it") }
      intent.putExtra(EXTRA_WORDS_STRING, sb.deleteCharAt(0).toString())
      intent.putExtra(EXTRA_LEVEL, level)
      intent.putExtra(EXTRA_PINYIN_STRING, unaskedWord.pinyin)
      intent.putExtra(EXTRA_REMAINING_QUESTION_COUNT, unaskedWords.size)
      intent.putExtra(EXTRA_BYTE_ARRAY, myCanvasView.getByteArray())
      intent.putExtra(EXTRA_HSK_ID, unaskedWord.index)

      // todo: What to do with intent.putExtra(EXTRA_QUIZ_ITEM, quizItem) in QuestionActivity.java?

      startActivity(intent)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_question, menu)
    return true
  }

  fun openMissingSoundDialog(item: MenuItem?) {
    AlertDialog.Builder(this)
      .setTitle(R.string.missing_sound)
      .setMessage("Set preferred engine to \"Google Text-to-speech Engine.\"")
      .setPositiveButton(
        "Open phone speech settings"
      ) { _, _ -> startActivity(MainActivity.openSpeechSettingsHelper()) }
      .setNegativeButton(
        "No need. I can heard the words."
      ) { dialog: DialogInterface, _ -> dialog.dismiss() }
      .show()
  }

  companion object {
    const val EXTRA_HSK_ID = "com.zhiyong.tingxie.ui.question.extra.HSK_ID"
  }
}
