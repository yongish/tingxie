package com.zhiyong.tingxie.ui.exercises_completed

import android.os.Bundle
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.DrawerActivity

class ExercisesCompletedActivity : DrawerActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, ExercisesCompletedFragment.newInstance())
        .commitNow()
    }
  }
}