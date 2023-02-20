package com.zhiyong.tingxie.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.firebase.ui.auth.AuthUI
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.databinding.ActivityDrawerBinding
import com.zhiyong.tingxie.ui.exercises_completed.ExercisesCompletedActivity
import com.zhiyong.tingxie.ui.hsk.buttons.HskButtonsActivity
import com.zhiyong.tingxie.ui.login.LoginActivity
import com.zhiyong.tingxie.ui.main.DataModel
import com.zhiyong.tingxie.ui.main.DrawerItemCustomAdapter
import com.zhiyong.tingxie.ui.main.MainActivity
import com.zhiyong.tingxie.ui.profile.ProfileActivity

abstract class DrawerActivity : AppCompatActivity() {

  private var _binding: ActivityDrawerBinding? = null
  private val binding get() = _binding!!

  private lateinit var mDrawerList: ListView
  private lateinit var mDrawerLayout: DrawerLayout
  private lateinit var mDrawerToggle: ActionBarDrawerToggle
  private lateinit var toolbar: Toolbar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    _binding = ActivityDrawerBinding.inflate(layoutInflater)
    setContentView(binding.root)
    toolbar = binding.toolbar

    setSupportActionBar(toolbar)

    mDrawerList = binding.leftDrawer
    val drawerItem = arrayOf(
      DataModel(R.drawable.ic_launcher_foreground, "听写"),
      DataModel(R.drawable.ic_baseline_school_black_24, "Exercises"),
      DataModel(R.drawable.ic_baseline_person_black_24, "Profile"),
      DataModel(R.drawable.ic_baseline_view_list_black_24, "HSK Lists"),
      DataModel(R.drawable.ic_exit_black_24, "Logout"),
    )

    val adapter = DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem)
    mDrawerList.adapter = adapter
    mDrawerList.setOnItemClickListener { _, _, i, _ -> selectItem(i) }
    mDrawerLayout = binding.drawerLayout
    setupDrawerToggle()
    mDrawerLayout.addDrawerListener(mDrawerToggle)
  }

  private fun selectItem(position: Int) {
    mDrawerLayout.closeDrawer(mDrawerList)
    when (position) {
      0 -> startActivity(Intent(this, MainActivity::class.java))
      1 -> startActivity(Intent(this, ExercisesCompletedActivity::class.java))
      2 -> startActivity(Intent(this, ProfileActivity::class.java))
      3 -> {
        startActivity(Intent(this, HskButtonsActivity::class.java))
        AuthUI.getInstance()
          .signOut(this)
          .addOnCompleteListener {
            startActivity(
              Intent(
                this,
                LoginActivity::class.java
              )
            )
            finish()
          }
      }
      4 -> AuthUI.getInstance()
        .signOut(this)
        .addOnCompleteListener {
          startActivity(Intent(this, LoginActivity::class.java))
          finish()
        }
      else -> {}
    }
  }

  private fun setupDrawerToggle() {
    mDrawerToggle = ActionBarDrawerToggle(
      this, mDrawerLayout, toolbar,
      R.string.app_name, R.string.app_name
    )
    //This is necessary to change the icon of the Drawer Toggle upon state change.
    mDrawerToggle.syncState()
  }
}