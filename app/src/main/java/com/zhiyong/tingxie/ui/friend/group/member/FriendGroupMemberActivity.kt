package com.zhiyong.tingxie.ui.friend.group.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.friend.group.name.FriendGroupNameAdapter.Companion.EXTRA_GROUP
import com.zhiyong.tingxie.ui.friend.group.name.TingXieGroup

class FriendGroupMemberActivity : AppCompatActivity() {

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

      val groupMemberFragment = FriendGroupMemberFragment.newInstance()
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