package com.zhiyong.tingxie.ui.hsk.words

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsFragment.Companion.EXTRA_LEVEL

class HskWordsActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.hsk_words_activity)
    title = "HSK level ${intent.getIntExtra(EXTRA_LEVEL, 0)}"
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, HskWordsFragment.newInstance())
        .commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_hsk_list, menu)
    return true
  }

  fun openHelpHsk(item: android.view.MenuItem) {
    val builder: AlertDialog.Builder = this.let {
      AlertDialog.Builder(it)
    }
    builder.setMessage("Tap on a word to search for it in Baidu dictionary.")
      .setTitle("Words are tappable")
    builder.create().show()
  }
}