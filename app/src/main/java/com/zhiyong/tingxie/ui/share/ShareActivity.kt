package com.zhiyong.tingxie.ui.share

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
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
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    binding = ShareActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val quizId = intent.getLongExtra(EXTRA_QUIZ_ID, -1)
    val bundle = Bundle()
    bundle.putLong(EXTRA_QUIZ_ID, quizId)
    binding.vpShare.adapter = ShareAdapter(this, 2, bundle)
    TabLayoutMediator(binding.tabLayout, binding.vpShare) { tab, position ->
      if (position == 0) {
        tab.text = "Individuals"
      } else {
        tab.text = "Groups"
      }
    }.attach()
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }
}