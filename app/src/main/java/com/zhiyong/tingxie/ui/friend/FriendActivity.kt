package com.zhiyong.tingxie.ui.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FriendActivityBinding
import com.zhiyong.tingxie.ui.Util

class FriendActivity : AppCompatActivity() {

  private lateinit var binding: FriendActivityBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (!Util.isOnline(this)) {
      Toast.makeText(this, "Internet connection required.", Toast.LENGTH_LONG).show()
    }

    setContentView(R.layout.friend_activity)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    binding = FriendActivityBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    val friendAdapter = FriendAdapter(this, 3)
    binding.vpFriend.adapter = friendAdapter

    TabLayoutMediator(binding.tabLayout, binding.vpFriend) { tab, position ->
      when (position) {
        0 -> tab.text = "Friends"
        1 -> tab.text = "Pending requests"
        2 -> tab.text = "Friend requests received"
      }
    }.attach()
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_help, menu)
    return true
  }

  fun openHelp(item: android.view.MenuItem) {
    val builder: AlertDialog.Builder = this.let {
      AlertDialog.Builder(it)
    }
    builder.setMessage(HtmlCompat.fromHtml(
        """
          Add other 听写 users as friends and share quizzes with them.
          The various tabs in this page are as follows.<br /><br />
          <b>Friends</b>: View and manage your friends here.<br /><br />
          <b>Pending requests</b>: See your pending friend requests to others.<br /><br />
          <b>Friend requests received</b>: Grant or deny friend requests other users made to you.<br /><br />
          Need help? Email Zhiyong with your feedback and questions.
      """, HtmlCompat.FROM_HTML_MODE_LEGACY))
        .setTitle("What is Friends?")
        .setPositiveButton("Email Zhiyong") {
          _, _ -> Util.emailZhiyong(this)
        }
        .setNegativeButton("Close") { dialog, _ -> dialog.dismiss() }
        .create().show()
  }
}