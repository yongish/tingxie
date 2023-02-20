package com.zhiyong.tingxie.ui.hsk.buttons

import android.os.Bundle
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.DrawerActivity

class HskButtonsActivity : DrawerActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    setContentView(R.layout.hsk_buttons_activity)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, HskButtonsFragment.newInstance())
        .commitNow()
    }
//    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }
}