package com.zhiyong.tingxie.ui.question

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.network.NetworkQuiz
import com.zhiyong.tingxie.ui.answer.RemoteAnswerActivity
import com.zhiyong.tingxie.ui.main.MainActivity
import java.util.*

class RemoteQuestionActivity : AppCompatActivity() {
  private lateinit var btnErase: Button
  private lateinit var btnReset: Button
  private lateinit var btnShowAnswer: Button
  private lateinit var ivPlay: ImageView
  private lateinit var myCanvasView: MyCanvasView
  private lateinit var textToSpeech: TextToSpeech
  private lateinit var tvQuestionPinyin: TextView
  private lateinit var tvRemaining: TextView

  private lateinit var mQuestionViewModel: QuestionViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_question)

    val toolbar: Toolbar = findViewById(R.id.toolbar)
    setSupportActionBar(toolbar)
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    toolbar.setNavigationOnClickListener {
      startActivity(Intent(this, MainActivity::class.java))
    }

    // Same behavior for user-defined quiz and HSK.
    myCanvasView = findViewById(R.id.view)

    btnErase = findViewById(R.id.btnErase)
    btnErase.setOnClickListener {
      myCanvasView.erase()
    }

    ivPlay = findViewById(R.id.ivPlay)
    tvQuestionPinyin = findViewById(R.id.tvQuestionPinyin)


    tvRemaining = findViewById(R.id.tvRemaining)

    // Different behavior for user-defined quiz and HSK.
    btnReset = findViewById(R.id.btnReset)
    btnShowAnswer = findViewById(R.id.btnShowAnswer)

    // From AnswerActivity.
    val quizItem: NetworkQuiz? =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(EXTRA_QUIZ_ITEM, NetworkQuiz::class.java)
      } else {
        intent.getParcelableExtra(EXTRA_QUIZ_ITEM)
      }
    if (quizItem != null) {
      val viewModelFactory = QuestionViewModelFactory(this.application, quizItem.quizId)
      mQuestionViewModel =
        ViewModelProvider(this, viewModelFactory)[QuestionViewModel::class.java]
      mQuestionViewModel.notCorrectWordsRandomOrder.observe(this) { words ->
        val word = words[0]

        textToSpeech = TextToSpeech(applicationContext) { status ->
          if (status != TextToSpeech.ERROR) {
            //if there is no error then set language
            textToSpeech.language = Locale.SIMPLIFIED_CHINESE
            speak(textToSpeech, word.characters)
          }
        }

        ivPlay.setOnClickListener { speak(textToSpeech, word.characters) }

        tvQuestionPinyin.text = word.pinyin

        tvRemaining.text = this.getString(R.string.remaining, quizItem.numNotCorrect, quizItem.numWords)

        btnReset.visibility = View.GONE
        btnShowAnswer.setOnClickListener {
          val intent = Intent(applicationContext, RemoteAnswerActivity::class.java)
          intent.putExtra(EXTRA_QUIZ_ITEM, quizItem)
          intent.putExtra(EXTRA_WORD_ID, word.id)
          intent.putExtra(EXTRA_WORD_STRING, word.characters)
          intent.putExtra(EXTRA_PINYIN_STRING, word.pinyin)
          intent.putExtra(EXTRA_REMAINING_QUESTION_COUNT, words.size)
          intent.putExtra(EXTRA_BYTE_ARRAY, myCanvasView.getByteArray())
          startActivity(intent)
        }
      }
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
      .create().show()
  }

  companion object {
    const val EXTRA_QUIZ_ITEM = "com.zhiyong.tingxie.ui.question.extra.QUIZ_ITEM"
    const val EXTRA_WORD_ID = "com.zhiyong.tingxie.ui.question.extra.WORD_ID"
    const val EXTRA_WORD_STRING = "com.zhiyong.tingxie.ui.question.extra.WORD_STRING"
    const val EXTRA_PINYIN_STRING = "com.zhiyong.tingxie.ui.question.extra.PINYIN_STRING"
    const val EXTRA_REMAINING_QUESTION_COUNT =
      "com.zhiyong.tingxie.ui.question.extra.REMAINING_QUESTION_COUNT"
    const val EXTRA_BYTE_ARRAY = "com.zhiyong.tingxie.ui.question.extra.BYTE_ARRAY"

    fun speak(textToSpeech: TextToSpeech, hanzi: String) {
      textToSpeech.speak(hanzi, TextToSpeech.QUEUE_FLUSH, null, null)
    }
  }
}
