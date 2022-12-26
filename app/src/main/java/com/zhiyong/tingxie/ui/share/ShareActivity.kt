package com.zhiyong.tingxie.ui.share

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ShareActivityBinding
import com.zhiyong.tingxie.ui.main.MainActivity

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
    binding.vpShare.adapter = ShareAdapter(this, 2, quizId, )
    TabLayoutMediator(binding.tabLayout, binding.vpShare) { tab, position ->
      when (position) {
        0 -> tab.text = "Individuals"
        1 -> tab.text = "Groups"
      }
    }.attach()
  }

  override fun onSupportNavigateUp(): Boolean {
    startActivity(Intent(this, MainActivity::class.java))
    return true
  }
}
