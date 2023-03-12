package com.zhiyong.tingxie.ui.add_quiz_individual

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ActivityAddQuizIndividualBinding

class AddQuizIndividualActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAddQuizIndividualBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityAddQuizIndividualBinding.inflate(layoutInflater)
    setContentView(binding.root)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, AddQuizIndividualFragment.newInstance())
        .commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressedDispatcher.onBackPressed()
    return true
  }
}
