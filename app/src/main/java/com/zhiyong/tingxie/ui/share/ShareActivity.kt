package com.zhiyong.tingxie.ui.share

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ShareActivityBinding

class ShareActivity : AppCompatActivity() {

  companion object {
    const val EXTRA_QUIZ_ID = "com.zhiyong.tingxie.ui.share.extra.QUIZ_ID"
  }

  private lateinit var binding: ShareActivityBinding

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
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    binding = ShareActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }
}