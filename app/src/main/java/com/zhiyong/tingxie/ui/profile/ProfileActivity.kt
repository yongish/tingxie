package com.zhiyong.tingxie.ui.profile

import android.os.Bundle
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.DrawerActivity

class ProfileActivity : DrawerActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, ProfileFragment.newInstance())
        .commitNow()
    }
  }
}