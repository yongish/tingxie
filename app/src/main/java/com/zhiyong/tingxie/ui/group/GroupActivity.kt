package com.zhiyong.tingxie.ui.group

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ActivityGroupBinding
import com.zhiyong.tingxie.ui.Util

class GroupActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var binding: ActivityGroupBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (!Util.isOnline(this)) {
      Toast.makeText(this, "Internet connection required.", Toast.LENGTH_LONG).show()
    }

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, GroupFragment.newInstance())
        .commitNow()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

//    binding = ActivityGroupBinding.inflate(layoutInflater)
//    setContentView(binding.root)
//
//    setSupportActionBar(binding.toolbar)
//
//    val navController = findNavController(R.id.nav_host_fragment_content_group)
//    appBarConfiguration = AppBarConfiguration(navController.graph)
//    setupActionBarWithNavController(navController, appBarConfiguration)
//
//    binding.fab.setOnClickListener { view ->
//      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//        .setAction("Action", null).show()
//    }
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_group)
    return navController.navigateUp(appBarConfiguration)
        || super.onSupportNavigateUp()
  }

  companion object {
    const val EXTRA_NETWORK_GROUP = "com.zhiyong.tingxie.ui.group.extra.EXTRA_NETWORK_GROUP"
  }
}