package com.zhiyong.tingxie.ui.group_membership

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ActivityGroupMembershipBinding

class GroupMembershipActivity : AppCompatActivity() {

  private lateinit var binding: ActivityGroupMembershipBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityGroupMembershipBinding.inflate(layoutInflater)
    setContentView(binding.root)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, GroupMembershipFragment.newInstance())
        .commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressedDispatcher.onBackPressed()
    return true
  }
}
