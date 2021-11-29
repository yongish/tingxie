package com.zhiyong.tingxie.ui.share

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhiyong.tingxie.R

class ShareActivity : AppCompatActivity() {

  companion object {
    const val EXTRA_QUIZ_ID = "com.zhiyong.tingxie.ui.share.extra.QUIZ_ID"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.share_activity)

    val quizId = intent.getLongExtra(EXTRA_QUIZ_ID, -1)
    val bundle = Bundle()
    bundle.putLong(EXTRA_QUIZ_ID, quizId)
    val shareFragment = ShareFragment()
    shareFragment.arguments = bundle

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
              .replace(R.id.container, shareFragment)
              .commitNow()
    }
  }
}