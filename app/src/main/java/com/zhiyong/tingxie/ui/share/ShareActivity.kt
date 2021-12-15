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

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.container, ShareIndividualFragment.newInstance())
          .commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }
}