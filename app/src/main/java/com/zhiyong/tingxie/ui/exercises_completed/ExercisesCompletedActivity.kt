package com.zhiyong.tingxie.ui.exercises_completed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhiyong.tingxie.R

class ExercisesCompletedActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_exercises_completed)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, ExercisesCompletedFragment.newInstance())
        .commitNow()
    }
  }
}