package com.zhiyong.tingxie.ui.select_quiz_member_type

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ActivitySelectQuizMemberTypeBinding

class SelectQuizMemberTypeActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySelectQuizMemberTypeBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySelectQuizMemberTypeBinding.inflate(layoutInflater)
    setContentView(binding.root)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, SelectQuizMemberTypeFragment.newInstance())
        .commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressedDispatcher.onBackPressed()
    return true
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_help, menu)
    return true
  }

  fun openHelp(item: android.view.MenuItem) {
    val builder: AlertDialog.Builder = this.let {
      AlertDialog.Builder(it)
    }
    builder.setMessage(
      """
      1. Group - Share the quiz with group, so everyone in the group can access it.
      2. Person - Share the quiz with a person.
      """.trimIndent()
    )
      .setTitle("Share with group or person")
      .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
      .create().show()
  }
}
