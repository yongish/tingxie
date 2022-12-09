package com.zhiyong.tingxie.ui.group_member

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ActivityGroupMemberBinding
import com.zhiyong.tingxie.ui.Util

class GroupMemberActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (!Util.isOnline(this)) {
      Toast.makeText(this, "Internet connection required.", Toast.LENGTH_LONG).show()
    }

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, GroupMemberFragment.newInstance())
        .commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

//    private lateinit var binding: ActivityGroupMemberBinding
//    binding = ActivityGroupMemberBinding.inflate(layoutInflater)
//    setContentView(binding.root)
//
//    setSupportActionBar(binding.toolbar)
//
//    val navController = findNavController(R.id.nav_host_fragment_content_group_member)
//    appBarConfiguration = AppBarConfiguration(navController.graph)
//    setupActionBarWithNavController(navController, appBarConfiguration)
//
//    binding.fab.setOnClickListener { view ->
//      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//        .setAction("Action", null).show()
//    }
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_group_member)
    return navController.navigateUp(appBarConfiguration)
        || super.onSupportNavigateUp()
  }
}