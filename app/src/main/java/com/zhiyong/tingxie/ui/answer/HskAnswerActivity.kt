package com.zhiyong.tingxie.ui.answer

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsFragment.Companion.EXTRA_LEVEL
import com.zhiyong.tingxie.ui.hsk.words.HskWordsViewModel
import com.zhiyong.tingxie.ui.main.MainActivity
import com.zhiyong.tingxie.ui.question.HskQuestionActivity

class HskAnswerActivity : AppCompatActivity() {
  private lateinit var tvAnswerWords: TextView
  private lateinit var btnAnswerCorrect: Button
  private lateinit var btnAnswerWrong: Button
  private lateinit var ivAnswer: ImageView
  private lateinit var viewModel: HskWordsViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_answer)

    tvAnswerWords = findViewById(R.id.tvAnswerWords)
    btnAnswerCorrect = findViewById(R.id.btnAnswerCorrect)
    btnAnswerWrong = findViewById(R.id.btnAnswerWrong)
    ivAnswer = findViewById(R.id.ivAnswer)

    viewModel = ViewModelProvider(this).get(HskWordsViewModel::class.java)

    val wordsString = intent.getStringExtra(HskQuestionActivity.EXTRA_WORDS_STRING)

    val ba = intent.getByteArrayExtra(HskQuestionActivity.EXTRA_BYTE_ARRAY)!!
    val b = BitmapFactory.decodeByteArray(ba, 0, ba.size)
    ivAnswer.setImageBitmap(b)

    tvAnswerWords.text = wordsString
    tvAnswerWords.setOnClickListener {
      val url = "https://baike.baidu.com/item/$wordsString"
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse(url)
      startActivity(intent)
    }

    val remainingCount = intent.getIntExtra(
      HskQuestionActivity.EXTRA_REMAINING_QUESTION_COUNT, -1
    )

    val intentQuestion = Intent(this, HskQuestionActivity::class.java)
    val level = intent.getIntExtra(EXTRA_LEVEL, -3)
    val hskIndex = intent.getIntExtra(HskQuestionActivity.EXTRA_HSK_ID, 1)

    intentQuestion.putExtra(EXTRA_LEVEL, level)
    btnAnswerCorrect.setOnClickListener {
      viewModel.setAsked(hskIndex)
      if (remainingCount < 2) {
        viewModel.resetAsked(level)

        AlertDialog.Builder(this)
          .setTitle("Round Completed.")
          .setMessage("Great. You completed a round with all questions correct.")
          .setPositiveButton("Next round") { _, _ -> startActivity(intentQuestion) }
          .setNegativeButton("Main menu") { _, _ ->
            startActivity(Intent(applicationContext, MainActivity::class.java))
          }
          .show()
      } else {
        Toast.makeText(this, "Good", Toast.LENGTH_SHORT).show()
        startActivity(intentQuestion)
      }
    }

    btnAnswerWrong.setOnClickListener {
      Toast.makeText(
        this, "Keep going.",
        Toast.LENGTH_SHORT
      ).show()
      startActivity(intentQuestion)
    }
  }
}
