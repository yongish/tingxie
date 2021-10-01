package com.zhiyong.tingxie.ui.hsk.buttons

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhiyong.tingxie.R

class HskButtonsActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.hsk_buttons_activity)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, HskButtonsFragment.newInstance())
        .commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

}