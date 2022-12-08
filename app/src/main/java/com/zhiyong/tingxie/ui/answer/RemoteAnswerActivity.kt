package com.zhiyong.tingxie.ui.answer

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.network.NetworkQuiz
import com.zhiyong.tingxie.ui.hsk.words.HskWordsViewModel
import com.zhiyong.tingxie.ui.main.MainActivity
import com.zhiyong.tingxie.ui.main.QuizItem
import com.zhiyong.tingxie.ui.question.RemoteQuestionActivity

class RemoteAnswerActivity : AppCompatActivity() {
  private lateinit var tvAnswerWords: TextView
  private lateinit var btnAnswerCorrect: Button
  private lateinit var btnAnswerWrong: Button
  private lateinit var ivAnswer: ImageView

  private lateinit var mAnswerViewModel: AnswerViewModel
  private lateinit var viewModel: HskWordsViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_answer)

    tvAnswerWords = findViewById(R.id.tvAnswerWords)
    btnAnswerCorrect = findViewById(R.id.btnAnswerCorrect)
    btnAnswerWrong = findViewById(R.id.btnAnswerWrong)
    ivAnswer = findViewById(R.id.ivAnswer)

    viewModel = ViewModelProvider(this)[HskWordsViewModel::class.java]

    val ba = intent.getByteArrayExtra(RemoteQuestionActivity.EXTRA_BYTE_ARRAY)!!
    val b = BitmapFactory.decodeByteArray(ba, 0, ba.size)
    ivAnswer.setImageBitmap(b)

    val remainingCount = intent.getIntExtra(
      RemoteQuestionActivity.EXTRA_REMAINING_QUESTION_COUNT, -1
    )

    val wordString = intent.getStringExtra(RemoteQuestionActivity.EXTRA_WORD_STRING)
    tvAnswerWords.text = wordString
    tvAnswerWords.setOnClickListener {
      val url = "https://baike.baidu.com/item/$wordString"
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse(url)
      startActivity(intent)
    }

    val quizItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      intent.getParcelableExtra(
        RemoteQuestionActivity.EXTRA_QUIZ_ITEM,
        QuizItem::class.java
      )
    } else {
      intent.getParcelableExtra(RemoteQuestionActivity.EXTRA_QUIZ_ITEM)
    }
    val intentQuestion = Intent(this, RemoteQuestionActivity::class.java)

    btnAnswerCorrect.setOnClickListener {
      if (quizItem != null) {
        quizItem.numNotCorrect = quizItem.numNotCorrect - 1
      }
      intentQuestion.putExtra(
        RemoteQuestionActivity.EXTRA_QUIZ_ITEM,
        quizItem
      )

      mAnswerViewModel = ViewModelProviders.of(this)[AnswerViewModel::class.java]
      mAnswerViewModel.upsertCorrectRecord(
        intent.getLongExtra(RemoteQuestionActivity.EXTRA_WORD_ID, -1)
      )

      // Go to Completed Alert Dialog or QuestionActivity.
      // Was this the last word in current round?
      Log.d("REMAINING_QN", remainingCount.toString())

      if (remainingCount < 2) {
        if (quizItem != null) {
          quizItem.numNotCorrect = quizItem.numWords
          quizItem.round += 1
          mAnswerViewModel.updateQuiz(quizItem)
        }
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
      intentQuestion.putExtra(
        RemoteQuestionActivity.EXTRA_QUIZ_ITEM,
        quizItem
      )
      Toast.makeText(
        this, "Keep going.",
        Toast.LENGTH_SHORT
      ).show()
      startActivity(intentQuestion)
    }
  }
}
