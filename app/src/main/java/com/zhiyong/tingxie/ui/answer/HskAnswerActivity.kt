package com.zhiyong.tingxie.ui.answer

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.db.Question.QuestionBuilder
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsFragment.Companion.EXTRA_LEVEL
import com.zhiyong.tingxie.ui.hsk.words.HskWordsViewModel
import com.zhiyong.tingxie.ui.main.MainActivity
import com.zhiyong.tingxie.ui.main.QuizItem
import com.zhiyong.tingxie.ui.question.QuestionActivity
import com.zhiyong.tingxie.ui.question.QuestionActivity.Companion.EXTRA_QUIZ_ITEM
import com.zhiyong.tingxie.ui.word.WordItem

class HskAnswerActivity: AppCompatActivity() {
  private lateinit var viewModel: HskWordsViewModel

  private lateinit var tvAnswerWords: TextView
  private lateinit var btnAnswerCorrect: Button
  private lateinit var btnAnswerWrong: Button
  private lateinit var ivAnswer: ImageView

  private lateinit var mAnswerViewModel: AnswerViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_answer)

    tvAnswerWords = findViewById(R.id.tvAnswerWords)
    btnAnswerCorrect = findViewById(R.id.btnAnswerCorrect)
    btnAnswerWrong = findViewById(R.id.btnAnswerWrong)
    ivAnswer = findViewById(R.id.ivAnswer)

    viewModel = ViewModelProvider(this).get(HskWordsViewModel::class.java)

    val wordsString = intent.getStringExtra(QuestionActivity.EXTRA_WORDS_STRING)
    val pinyinString = intent.getStringExtra(QuestionActivity.EXTRA_PINYIN_STRING)

    val ba = intent.getByteArrayExtra(QuestionActivity.EXTRA_BYTE_ARRAY)!!
    val b = BitmapFactory.decodeByteArray(ba, 0, ba.size)
    ivAnswer.setImageBitmap(b)

    tvAnswerWords.text = wordsString
    tvAnswerWords.setOnClickListener {
      val url = "https://baike.baidu.com/item/$wordsString"
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse(url)
      startActivity(intent)
    }

    val quizItem: QuizItem? = intent.getParcelableExtra(EXTRA_QUIZ_ITEM)
    if (quizItem == null) {
      val level = intent.getIntExtra(EXTRA_LEVEL, -3)
      val hskIndex = intent.getIntExtra(QuestionActivity.EXTRA_HSK_ID, 1)

      val intentQuestion = Intent(this, QuestionActivity::class.java)
      intentQuestion.putExtra(EXTRA_LEVEL, level)
      btnAnswerCorrect.setOnClickListener {
        viewModel.setAsked(hskIndex)
        val remainingCount = intent.getIntExtra(QuestionActivity.EXTRA_REMAINING_QUESTION_COUNT, -1)
        if (remainingCount < 2) {
          viewModel.resetAsked(level)

          val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
          }

          builder.setMessage(
            "Great. You completed a round with all questions correct."
          )
            .setTitle("Round Completed.")
            .setPositiveButton("Next round") { _, _ -> startActivity(intentQuestion) }
            .setNegativeButton("Main menu") { _, _ ->
              startActivity(Intent(this, MainActivity::class.java))
            }
            .create().show()
        } else {
          Toast.makeText(this, "Good", Toast.LENGTH_SHORT).show()
          startActivity(intentQuestion)
        }
      }

      btnAnswerWrong.setOnClickListener {
        startActivity(intentQuestion)
      }

    } else {

      mAnswerViewModel = ViewModelProviders.of(this).get(
        AnswerViewModel::class.java
      )

      val wordsString = intent.getStringExtra(QuestionActivity.EXTRA_WORDS_STRING)
      val pinyinString = intent.getStringExtra(QuestionActivity.EXTRA_PINYIN_STRING)

      ivAnswer = findViewById(R.id.ivAnswer)
      val ba = intent.getByteArrayExtra(QuestionActivity.EXTRA_BYTE_ARRAY)!!
      val b = BitmapFactory.decodeByteArray(ba, 0, ba!!.size)
      ivAnswer.setImageBitmap(b)

      tvAnswerWords = findViewById(R.id.tvAnswerWords)
      tvAnswerWords.text = wordsString
      tvAnswerWords.setOnClickListener {
        val url = "https://baike.baidu.com/item/$wordsString"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
      }

      btnAnswerCorrect = findViewById(R.id.btnAnswerCorrect)
      val ts = System.currentTimeMillis()
      assert(quizItem != null)
      val questionBuilder = QuestionBuilder()
        .timestamp(ts)
        .pinyinString(pinyinString)
        .quizId(quizItem!!.id)
      val intentQuestion = Intent(
        applicationContext,
        QuestionActivity::class.java
      )
      intentQuestion.putExtra("quiz", quizItem)
      btnAnswerCorrect.setOnClickListener { v: View? ->
        // Insert new question with boolean correct.
        val question =
          questionBuilder.correct(true).build()
        mAnswerViewModel.onAnswer(
          question, WordItem(
            quizItem!!.id, wordsString, pinyinString, true
          )
        )
        quizItem!!.notLearned = quizItem!!.notLearned - 1
        // Go to Completed Alert Dialog or QuestionActivity.
        // Was this the last word in current round?
        val remainingQuestionCount = intent
          .getIntExtra(QuestionActivity.EXTRA_REMAINING_QUESTION_COUNT, -1)
        Log.d("REMAINING_QN", remainingQuestionCount.toString())
        if (remainingQuestionCount < 2) {
          quizItem!!.round = quizItem!!.round + 1
          quizItem!!.notLearned = quizItem!!.totalWords
          mAnswerViewModel.resetAsked(quizItem!!.id)
          AlertDialog.Builder(this)
            .setTitle("Round Completed.")
            .setMessage("Great. You completed a round with all questions correct.")
            .setPositiveButton(
              "Next round"
            ) { dialog: DialogInterface?, which: Int ->
              val intent = Intent(
                applicationContext,
                QuestionActivity::class.java
              )
              intent.putExtra("quiz", quizItem)
              startActivity(intent)
            }
            .setNegativeButton(
              "Main menu"
            ) { dialog: DialogInterface?, which: Int ->
              startActivity(
                Intent(
                  applicationContext,
                  MainActivity::class.java
                )
              )
            }
            .show()
        } else {
          Toast.makeText(
            this, "Good",
            Toast.LENGTH_SHORT
          ).show()
          startActivity(intentQuestion)
        }
        mAnswerViewModel.updateQuiz(quizItem)
      }
      btnAnswerWrong = findViewById(R.id.btnAnswerWrong)
      btnAnswerWrong.setOnClickListener { v: View? ->
        // Insert new question with boolean wrong.
        val question =
          questionBuilder.correct(false).build()
        mAnswerViewModel.onAnswer(
          question, WordItem(
            quizItem!!.id, wordsString, pinyinString, false
          )
        )
        Toast.makeText(
          this, "Keep going.",
          Toast.LENGTH_SHORT
        ).show()

        // Go to QuestionActivity.
        startActivity(intentQuestion)
      }
    }

  }
}