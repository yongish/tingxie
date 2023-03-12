package com.zhiyong.tingxie.ui.add_quiz_group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ActivityAddQuizGroupBinding

class AddQuizGroupActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAddQuizGroupBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityAddQuizGroupBinding.inflate(layoutInflater)
    setContentView(binding.root)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, AddQuizGroupFragment.newInstance())
        .commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressedDispatcher.onBackPressed()
    return true
  }
}
