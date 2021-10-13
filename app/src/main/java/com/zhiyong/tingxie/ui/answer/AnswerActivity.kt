package com.zhiyong.tingxie.ui.answer

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
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsFragment.Companion.EXTRA_LEVEL
import com.zhiyong.tingxie.ui.hsk.words.HskWordsViewModel
import com.zhiyong.tingxie.ui.main.MainActivity
import com.zhiyong.tingxie.ui.main.QuizItem
import com.zhiyong.tingxie.ui.question.QuestionActivity
import com.zhiyong.tingxie.ui.question.QuestionActivity.Companion.EXTRA_QUIZ_ITEM
import com.zhiyong.tingxie.ui.word.WordItem

class AnswerActivity: AppCompatActivity() {
  private lateinit var tvAnswerWords: TextView
  private lateinit var btnAnswerCorrect: Button
  private lateinit var btnAnswerWrong: Button
  private lateinit var ivAnswer: ImageView

  private lateinit var mAnswerViewModel: AnswerViewModel
  private lateinit var viewModel: HskWordsViewModel

  private fun handleCorrect(
    remainingCount: Int,
    intentQuestion: Intent,
    level: Int = -1,
    quizItem: QuizItem? = null
  ) {
    if (remainingCount < 2) {
      if (quizItem == null) {
          viewModel.resetAsked(level)
      } else {
        quizItem.round = quizItem.round + 1
        quizItem.notLearned = quizItem.totalWords
        mAnswerViewModel.resetAsked(quizItem.id)
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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_answer)

    tvAnswerWords = findViewById(R.id.tvAnswerWords)
    btnAnswerCorrect = findViewById(R.id.btnAnswerCorrect)
    btnAnswerWrong = findViewById(R.id.btnAnswerWrong)
    ivAnswer = findViewById(R.id.ivAnswer)

    viewModel = ViewModelProvider(this).get(HskWordsViewModel::class.java)

    val wordsString = intent.getStringExtra(QuestionActivity.EXTRA_WORDS_STRING)

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

    val remainingCount = intent.getIntExtra(QuestionActivity.EXTRA_REMAINING_QUESTION_COUNT, -1)

    val quizItem: QuizItem? = intent.getParcelableExtra(EXTRA_QUIZ_ITEM)
    val intentQuestion = Intent(this, QuestionActivity::class.java)
    if (quizItem == null) {
      val level = intent.getIntExtra(EXTRA_LEVEL, -3)
      val hskIndex = intent.getIntExtra(QuestionActivity.EXTRA_HSK_ID, 1)

      intentQuestion.putExtra(EXTRA_LEVEL, level)
      btnAnswerCorrect.setOnClickListener {
        viewModel.setAsked(hskIndex)
        handleCorrect(remainingCount, intentQuestion, level = level)
      }

      btnAnswerWrong.setOnClickListener {
        Toast.makeText(
          this, "Keep going.",
          Toast.LENGTH_SHORT
        ).show()
        startActivity(intentQuestion)
      }

    } else {
      mAnswerViewModel = ViewModelProviders.of(this).get(
        AnswerViewModel::class.java
      )

      val pinyinString = intent.getStringExtra(QuestionActivity.EXTRA_PINYIN_STRING)

      val ts = System.currentTimeMillis()
      val questionBuilder = QuestionBuilder()
        .timestamp(ts)
        .pinyinString(pinyinString)
        .quizId(quizItem.id)
      intentQuestion.putExtra(EXTRA_QUIZ_ITEM, quizItem)
      btnAnswerCorrect.setOnClickListener {
        // Insert new question with boolean correct.
        val question = questionBuilder.correct(true).build()
        mAnswerViewModel.onAnswer(question, WordItem(
          quizItem.id, wordsString, pinyinString, true
        ))
        // Go to Completed Alert Dialog or QuestionActivity.
        // Was this the last word in current round?
        Log.d("REMAINING_QN", remainingCount.toString())
        handleCorrect(remainingCount, intentQuestion, quizItem = quizItem)
        mAnswerViewModel.updateQuiz(Quiz(
          quizItem.id, quizItem.date, quizItem.title,
          quizItem.totalWords, quizItem.notLearned - 1, quizItem.round
        ))
      }
      btnAnswerWrong.setOnClickListener {
        // Insert new question with boolean wrong.
        val question = questionBuilder.correct(false).build()
        mAnswerViewModel.onAnswer(question, WordItem(
          quizItem.id, wordsString, pinyinString, false
        ))
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