package com.zhiyong.tingxie.ui.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.FriendActivityBinding

class FriendActivity : AppCompatActivity() {

  private lateinit var binding: FriendActivityBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.friend_activity)
//    if (savedInstanceState == null) {
//      supportFragmentManager.beginTransaction()
//        .replace(R.id.container, IndividualFragment.newInstance())
//        .commitNow()
//    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    binding = FriendActivityBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    val friendAdapter = FriendAdapter(this, 2)
    binding.vpFriend.adapter = friendAdapter

    TabLayoutMediator(binding.tabLayout, binding.vpFriend) { tab, position ->
      if (position == 0) {
        tab.text = "Individuals"
      } else {
        tab.text = "Groups"
      }
    }.attach()
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }
}