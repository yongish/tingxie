package com.zhiyong.tingxie.ui.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.friend.GroupNameAdapter.Companion.EXTRA_GROUP

class GroupMemberActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.group_member_activity)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    val group = intent.getParcelableExtra<TingXieGroup>(EXTRA_GROUP)
    title = if (group?.name == null) {
      "Members"
      // Log to Crashlytics.
    } else {
      "Members of " + group.name
    }
    if (savedInstanceState == null) {
      val bundle = Bundle()
      bundle.putParcelable(EXTRA_GROUP, group)

      // todo: Also need to request friends, to add friends as members.

      val groupMemberFragment = GroupMemberFragment.newInstance()
      groupMemberFragment.arguments = bundle
      supportFragmentManager.beginTransaction()
          .replace(R.id.container, groupMemberFragment)
          .commitNow()
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }
}