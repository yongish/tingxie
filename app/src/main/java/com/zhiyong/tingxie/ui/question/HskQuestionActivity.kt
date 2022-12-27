package com.zhiyong.tingxie.ui.question

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.network.NetworkQuiz
import com.zhiyong.tingxie.network.NetworkWordItem
import com.zhiyong.tingxie.ui.answer.HskAnswerActivity
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsFragment.Companion.EXTRA_LEVEL
import com.zhiyong.tingxie.ui.hsk.words.HskWordsViewModel
import com.zhiyong.tingxie.ui.main.MainActivity
import java.util.*
import kotlin.text.StringBuilder

class HskQuestionActivity : AppCompatActivity() {
  private lateinit var btnErase: Button
  private lateinit var btnReset: Button
  private lateinit var btnShowAnswer: Button
  private lateinit var ivPlay: ImageView
  private lateinit var myCanvasView: MyCanvasView
  private lateinit var textToSpeech: TextToSpeech
  private lateinit var tvQuestionPinyin: TextView
  private lateinit var tvRemaining: TextView

  private lateinit var questionViewModel: HskWordsViewModel
  private lateinit var mQuestionViewModel: QuestionViewModel

  private fun initializeCommonComponents(
    hanzi: String, pinyin: String, numUnasked: Int, numTotal: Int?
  ) {
    // Same behavior for user-defined quiz and HSK.
    myCanvasView = findViewById(R.id.view)

    btnErase = findViewById(R.id.btnErase)
    btnErase.setOnClickListener { myCanvasView.erase() }

    textToSpeech = TextToSpeech(applicationContext) { status ->
      if (status != TextToSpeech.ERROR) {
        //if there is no error then set language
        textToSpeech.language = Locale.SIMPLIFIED_CHINESE
        speak(textToSpeech, hanzi)
      }
    }
    ivPlay = findViewById(R.id.ivPlay)
    ivPlay.setOnClickListener { speak(textToSpeech, hanzi) }

    tvQuestionPinyin = findViewById(R.id.tvQuestionPinyin)
    tvQuestionPinyin.text = pinyin

    tvRemaining = findViewById(R.id.tvRemaining)
    tvRemaining.text = this.getString(R.string.remaining, numUnasked, numTotal)

    // Different behavior for user-defined quiz and HSK.
    btnReset = findViewById(R.id.btnReset)
    btnShowAnswer = findViewById(R.id.btnShowAnswer)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_question)

    val toolbar: Toolbar = findViewById(R.id.toolbar)
    setSupportActionBar(toolbar)
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    toolbar.setNavigationOnClickListener {
      startActivity(Intent(this, MainActivity::class.java))
    }

    // From AnswerActivity.
    val quizItem: NetworkQuiz? = intent.getParcelableExtra(EXTRA_QUIZ_ITEM)
    if (quizItem == null) {
      questionViewModel = ViewModelProvider(this).get(HskWordsViewModel::class.java)

      val level = intent.getIntExtra(EXTRA_LEVEL, -2)
      val unaskedWords = questionViewModel.getUnaskedHskWords(level)
      val unaskedWord = unaskedWords.random()
      val numWordsInLevel = questionViewModel.getHsk(level).size
      initializeCommonComponents(
        unaskedWord.hanzi, unaskedWord.pinyin, unaskedWords.size, numWordsInLevel
      )

      // todo: not implemented for non-HSK QuestionActivity yet.
      btnReset.setOnClickListener {
        val isReset = questionViewModel.resetAsked(level)
        if (isReset) {
          tvRemaining.text = this.getString(
            R.string.remaining, numWordsInLevel, numWordsInLevel
          )
        }
      }

      val hanzisMatchingPinyin = questionViewModel.getHanzis(level, unaskedWord.pinyin)
      val sb = StringBuilder()
      hanzisMatchingPinyin.forEach { sb.append("\n$it") }

      btnShowAnswer.setOnClickListener {
        val intent = Intent(applicationContext, HskAnswerActivity::class.java)
        intent.putExtra(EXTRA_LEVEL, level)
        intent.putExtra(EXTRA_HSK_ID, unaskedWord.index)

        intent.putExtra(EXTRA_WORDS_STRING, sb.deleteCharAt(0).toString())
        intent.putExtra(EXTRA_PINYIN_STRING, unaskedWord.pinyin)
        intent.putExtra(EXTRA_REMAINING_QUESTION_COUNT, unaskedWords.size)
        intent.putExtra(EXTRA_BYTE_ARRAY, myCanvasView.getByteArray())
        startActivity(intent)
      }
    } else {
      mQuestionViewModel = ViewModelProviders
        .of(
          this,
          QuestionViewModelFactory(this.application, quizItem.quizId)
        )[QuestionViewModel::class.java]
      mQuestionViewModel.notCorrectWordsRandomOrder.observe(this) { words: List<NetworkWordItem> ->
        run {
          val word = words[0]
          initializeCommonComponents(
            word.characters,
            word.pinyin,
            quizItem.numNotCorrect,
            quizItem.numWords
          )
          btnReset.visibility = View.GONE
          btnShowAnswer.setOnClickListener {
            val intent = Intent(applicationContext, HskAnswerActivity::class.java)
            intent.putExtra(EXTRA_QUIZ_ITEM, quizItem)
            intent.putExtra(EXTRA_WORDS_STRING, word.characters)
            intent.putExtra(EXTRA_PINYIN_STRING, word.pinyin)
            intent.putExtra(EXTRA_REMAINING_QUESTION_COUNT, words.size)
            intent.putExtra(EXTRA_BYTE_ARRAY, myCanvasView.getByteArray())
            startActivity(intent)
          }
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
      .show()
  }

  companion object {
    const val EXTRA_HSK_ID = "com.zhiyong.tingxie.ui.question.extra.HSK_ID"

    const val EXTRA_QUIZ_ITEM = "com.zhiyong.tingxie.ui.question.extra.QUIZ_ITEM"
    const val EXTRA_WORDS_STRING = "com.zhiyong.tingxie.ui.question.extra.WORDS_STRING"
    const val EXTRA_PINYIN_STRING = "com.zhiyong.tingxie.ui.question.extra.PINYIN_STRING"
    const val EXTRA_REMAINING_QUESTION_COUNT =
      "com.zhiyong.tingxie.ui.question.extra.REMAINING_QUESTION_COUNT"
    const val EXTRA_BYTE_ARRAY = "com.zhiyong.tingxie.ui.question.extra.BYTE_ARRAY"

    fun speak(textToSpeech: TextToSpeech, hanzi: String) {
      textToSpeech.speak(hanzi, TextToSpeech.QUEUE_FLUSH, null, null)
    }
  }
}
