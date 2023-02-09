package com.zhiyong.tingxie

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.zhiyong.tingxie.ui.group.GroupActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.endsWith
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class GroupTest {
  @get:Rule
  val activityRule = ActivityScenarioRule(GroupActivity::class.java)

  @Before
  fun init() {
//    removeAllGroups(activityRule)
  }

  @After
  fun after() {
//    removeAllGroups(activityRule)
  }

  @Test
  fun createGroup() {
    onView(withId(R.id.fab)).perform(click())
    onView(withClassName(endsWith("EditText"))).perform(typeText("new group name"))
    onView(withText("OK")).perform(click())
    var itemCount = 0
    activityRule.scenario.onActivity { activityScenarioRule ->
      val recyclerView =
        activityScenarioRule.findViewById<RecyclerView>(R.id.recyclerview_readings)
      itemCount = recyclerView.adapter?.itemCount ?: 0
    }
    onView(withId(R.id.recyclerview_readings)).check(
      matches(
        atPosition(itemCount - 1, hasDescendant(withText("new group name")))
      )
    )
    onView(withId(R.id.recyclerview_readings)).check(
      matches(atPosition(itemCount - 1, hasDescendant(withText("OWNER"))))
    )
    onView(withId(R.id.recyclerview_readings)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        0, MyViewAction.clickChildViewWithId(R.id.btnMembers)
      )
    )
    onView(withId(R.id.recyclerview_group_members)).check(
      RecyclerViewItemCountAssertion(1)
    )
    onView(withId(R.id.fab)).perform(click())
    onView(withId(R.id.etEmail)).perform(replaceText("yongish@ymail.com"))
    onView(withId(R.id.rbMember)).perform(click())
    onView(withId(R.id.btnSubmit)).perform(click())

    onView(withId(R.id.recyclerview_group_members)).perform(waitUntilVisible(5000L))
    onView(withId(R.id.recyclerview_group_members)).check(
      RecyclerViewItemCountAssertion(2)
    )
    onView(withId(R.id.recyclerview_group_members)).check(
      matches(atPosition(1, hasDescendant(withText("yongish@ymail.com"))))
    )
    onView(withId(R.id.recyclerview_group_members)).check(
      matches(atPosition(1, hasDescendant(withText("MEMBER"))))
    )
    onView(withId(R.id.recyclerview_group_members)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        1, MyViewAction.clickChildViewWithId(R.id.ivDelete)
      )
    )
    onView(withId(android.R.id.button1)).perform(click())
    onView(withId(R.id.recyclerview_group_members)).perform(waitUntilVisible(5000L))
    onView(withId(R.id.recyclerview_group_members)).check(
      RecyclerViewItemCountAssertion(1)
    )
  }

  private fun waitUntilVisible(timeout: Long): ViewAction {
    return WaitUntilVisibleAction(timeout)
  }

  private fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getConstraints() = null
    override fun getDescription() = "Click on a child view with specified id."
    override fun perform(uiController: UiController, view: View) =
      click().perform(uiController, view.findViewById(viewId))
  }

  private fun atPosition(
    position: Int, itemMatcher: Matcher<View?>
  ): BoundedMatcher<View?, RecyclerView> {
    Preconditions.checkNotNull(itemMatcher)
    return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
      override fun describeTo(description: Description) {
        description.appendText("has item at position $position: ")
        itemMatcher.describeTo(description)
      }

      override fun matchesSafely(view: RecyclerView): Boolean {
        val viewHolder = view.findViewHolderForAdapterPosition(position)
          ?: // has no item on such position
          return false
        return itemMatcher.matches(viewHolder.itemView)
      }
    }
  }

  private fun removeAllGroups(activityRule: ActivityScenarioRule<GroupActivity>) {
    var count = 0
    runBlocking {
      activityRule.scenario.onActivity {
        val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerview_readings)
        count = recyclerView.adapter?.itemCount ?: 0
      }
    }
    try {
      repeat(count) {
        onView(withId(R.id.recyclerview_readings)).perform(
          RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            0, MyViewAction.clickChildViewWithId(R.id.btnMembers)
          )
        )

        var countMembers = 0
        runBlocking {
          activityRule.scenario.onActivity {
            val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerview_group_members)
            countMembers = recyclerView.adapter?.itemCount ?: 0
          }
        }
        repeat(countMembers - 1) {
          onView(withId(R.id.recyclerview_group_members)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
              0, MyViewAction.clickChildViewWithId(R.id.ivDelete)
            )
          )
          onView(withId(android.R.id.button1)).perform(click())
        }
        onView(withId(R.id.action_delete_group)).perform(click())
      }
    } catch (_: Exception) {

    }
  }
}

