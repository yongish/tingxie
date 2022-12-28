package com.zhiyong.tingxie.ui.share

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ActivityShareBinding
import com.zhiyong.tingxie.ui.main.MainActivity

class ShareActivity : AppCompatActivity() {

  private lateinit var binding: ActivityShareBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_share)

    binding = ActivityShareBinding.inflate(layoutInflater)
    setContentView(binding.root)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction().replace(R.id.container, ShareFragment.newInstance()).commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onSupportNavigateUp(): Boolean {
    startActivity(Intent(this, MainActivity::class.java))
    return true
  }
}
