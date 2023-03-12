package com.zhiyong.tingxie

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

class ScrollToBottomAction : ViewAction {
  override fun getDescription(): String {
    return "scroll RecyclerView to bottom"
  }

  override fun getConstraints(): Matcher<View> {
    return CoreMatchers.allOf<View>(
      ViewMatchers.isAssignableFrom(RecyclerView::class.java),
      ViewMatchers.isDisplayed()
    )
  }

  override fun perform(uiController: UiController?, view: View?) {
    val recyclerView = view as RecyclerView
    val itemCount = recyclerView.adapter?.itemCount
    val position = itemCount?.minus(1) ?: 0
    recyclerView.scrollToPosition(position)
    uiController?.loopMainThreadUntilIdle()
  }
}
