package com.zhiyong.tingxie.ui

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout

abstract class DrawerActivity(setBinding: Function<Toolbar>) : AppCompatActivity() {

  private lateinit var mDrawerList: ListView
  private lateinit var mDrawerLayout: DrawerLayout
  private lateinit var mDrawerToggle: ActionBarDrawerToggle
//  private var toolbar: Toolbar = setBinding()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }
}