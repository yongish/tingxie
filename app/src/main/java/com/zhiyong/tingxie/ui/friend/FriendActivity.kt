package com.zhiyong.tingxie.ui.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhiyong.tingxie.R

class FriendActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.friend_activity)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, IndividualFragment.newInstance())
        .commitNow()
    }
  }
}