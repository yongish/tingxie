package com.zhiyong.tingxie.ui.reading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ReadingActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_reading)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, ReadingFragment.newInstance())
        .commitNow()
    }
  }
}