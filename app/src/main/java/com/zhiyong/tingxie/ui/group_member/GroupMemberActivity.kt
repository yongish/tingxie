package com.zhiyong.tingxie.ui.group_member

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ActivityGroupMemberBinding
import com.zhiyong.tingxie.ui.Util
import com.zhiyong.tingxie.ui.group.GroupActivity

class GroupMemberActivity : AppCompatActivity() {

  private lateinit var binding: ActivityGroupMemberBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (!Util.isOnline(this)) {
      Toast.makeText(this, "Internet connection required.", Toast.LENGTH_LONG).show()
    }

    binding = ActivityGroupMemberBinding.inflate(layoutInflater)
    setContentView(binding.root)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, GroupMemberFragment.newInstance())
        .commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onSupportNavigateUp(): Boolean {
    startActivity(Intent(this, GroupActivity::class.java))
    return true
  }
}