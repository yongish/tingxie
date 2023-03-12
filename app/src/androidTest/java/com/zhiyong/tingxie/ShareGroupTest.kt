package com.zhiyong.tingxie

import android.content.Context
import android.view.View
import android.widget.DatePicker
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.zhiyong.tingxie.ui.main.MainActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class ShareGroupTest {
  @get:Rule
  val activityRule = ActivityScenarioRule(MainActivity::class.java)

  @Before
  fun init() {
    ApplicationProvider.getApplicationContext<Context>().deleteDatabase("pinyin_database")
    RecyclerViewTest.removeAllQuizzes(activityRule)
  }

  @After
  fun after() {
    RecyclerViewTest.removeAllQuizzes(activityRule)
  }

  @Test
  fun shareQuizWGroup() {
    // This test assumes that yongish@gmail.com and yongish@ymail.com are in Firebase.
    val c = Calendar.getInstance()
    addQuiz(c)

    openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext);
    onView(withText(R.string.groups)).perform(waitUntilVisible(10000L))
    onView(anyOf(withText(R.string.groups), withId(R.id.action_groups))).perform(click());
    onView(withId(R.id.fab)).perform(click())
    onView(withClassName(CoreMatchers.endsWith("EditText"))).perform(
      ViewActions.typeText("new group name")
    )
    onView(withText("OK")).perform(click())

    onView(isRoot()).perform(ViewActions.pressBack());

    onView(withId(R.id.recyclerview_main)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        0, clickOnViewChild(R.id.ivShare)
      )
    )
    onView(withId(R.id.recyclerview_shares)).check(ViewAssertions.matches(
        atPosition(0, hasDescendant(withText("Zhi Yong Tan")))
    ))
    onView(withId(R.id.recyclerview_shares)).check(ViewAssertions.matches(
        atPosition(0, hasDescendant(withText("OWNER")))
    ))
    onView(withId(R.id.fab)).perform(click())
    onView(withId(R.id.rbGroup)).perform(click())
    onView(withId(R.id.btnNext)).perform(click())
    onView(withId(R.id.btnChooseGroup)).perform(click())

//    Espresso.onView(ViewMatchers.withId(R.id.etEmail)).perform(ViewActions.replaceText("yongish@ymail.com"))
//    Espresso.onView(ViewMatchers.withId(R.id.btnSubmit)).perform(ViewActions.click())
//    Espresso.onView(ViewMatchers.withId(R.id.recyclerview_shares)).perform(waitUntilVisible(10000L))
//    Espresso.onView(ViewMatchers.withId(R.id.recyclerview_shares)).check(
//      RecyclerViewItemCountAssertion(2)
//    )
//    Espresso.onView(ViewMatchers.withId(R.id.recyclerview_shares)).check(
//      ViewAssertions.matches(
//        atPosition(1, ViewMatchers.hasDescendant(ViewMatchers.withText("Zhiyong Tan")))
//      )
//    )
//    Espresso.onView(ViewMatchers.withId(R.id.recyclerview_shares)).check(
//      ViewAssertions.matches(
//        atPosition(1, ViewMatchers.hasDescendant(ViewMatchers.withText("MEMBER")))
//      )
//    )
//    Espresso.onView(ViewMatchers.withId(R.id.recyclerview_shares)).perform(
//      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//        1, clickOnViewChild(R.id.ivDelete)
//      )
//    )
//    Espresso.onView(ViewMatchers.withId(android.R.id.button1))
//      .perform(ViewActions.click())
//    Espresso.onView(ViewMatchers.withId(R.id.recyclerview_shares)).check(
//      RecyclerViewItemCountAssertion(1)
//    )
  }

  private fun addQuiz(c: Calendar) =
    addQuiz(c[Calendar.YEAR], c[Calendar.MONTH] + 1, c[Calendar.DATE])

  private fun addQuiz(year: Int?, monthOfYear: Int?, dayOfMonth: Int?) {
    Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click())
    if (year != null && monthOfYear != null && dayOfMonth != null) {
      Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name)))
        .perform(PickerActions.setDate(year, monthOfYear, dayOfMonth))
    }
    Espresso.onView(ViewMatchers.withId(android.R.id.button1))
      .perform(ViewActions.click())
  }

  private fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getConstraints() = null
    override fun getDescription() = "Click on a child view with specified id."
    override fun perform(uiController: UiController, view: View) =
      ViewActions.click().perform(uiController, view.findViewById(viewId))
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

  private fun waitUntilVisible(timeout: Long): ViewAction {
    return WaitUntilVisibleAction(timeout)
  }
}