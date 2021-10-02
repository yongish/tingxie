package com.zhiyong.tingxie.ui.question

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsFragment.Companion.EXTRA_LEVEL

class HskQuestionActivity: AppCompatActivity() {
  private lateinit var questionViewModel: QuestionViewModel

  override fun onCreate(
    savedInstanceState: Bundle?,
    persistentState: PersistableBundle?
  ) {
    super.onCreate(savedInstanceState, persistentState)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    // todo: findViewById for various elements.

    questionViewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)
    questionViewModel
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

  // From HskButtonsActivity or HskAnswerActivity.
  val level = intent.getIntExtra(EXTRA_LEVEL, 1)


}