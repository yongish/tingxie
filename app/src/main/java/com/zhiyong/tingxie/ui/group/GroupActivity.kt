package com.zhiyong.tingxie.ui.group

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ActivityGroupBinding
import com.zhiyong.tingxie.ui.Util
import com.zhiyong.tingxie.ui.main.MainActivity

class GroupActivity : AppCompatActivity() {

  private lateinit var binding: ActivityGroupBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (!Util.isOnline(this)) {
      Toast.makeText(this, "Internet connection required.", Toast.LENGTH_LONG).show()
    }

    binding = ActivityGroupBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, GroupFragment.newInstance())
        .commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_help, menu)
    return true
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressedDispatcher.onBackPressed()
    return true
  }

  fun openHelp(item: android.view.MenuItem) {
    val builder: AlertDialog.Builder = this.let {
      AlertDialog.Builder(it)
    }
    builder.setMessage(
      """
      1. Tap on a word to search for it in Baidu dictionary.
      2. No sound on play button? Set preferred engine to "Google Text-to-speech Engine."
      """.trimIndent()
    )
      .setTitle("Tips")
      .setPositiveButton("Open phone speech settings") {
          _, _ -> startActivity(MainActivity.openSpeechSettingsHelper())
      }
      .setNegativeButton("No need. I can heard the words.") {
          dialog, _ -> dialog.dismiss()
      }
      .create().show()
  }

  companion object {
    const val EXTRA_NETWORK_GROUP = "com.zhiyong.tingxie.ui.group.extra.EXTRA_NETWORK_GROUP"
  }
}