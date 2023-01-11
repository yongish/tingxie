package com.zhiyong.tingxie

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.zhiyong.tingxie.ui.main.MainActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.*
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*


@LargeTest
@RunWith(AndroidJUnit4::class)
class RecyclerViewTest {
  private val floatingActionButton = fAB

  @get:Rule
  val activityRule = ActivityScenarioRule(MainActivity::class.java)

  @Before
  fun before() {
    ApplicationProvider.getApplicationContext<Context>()
      .deleteDatabase("pinyin_database")
//    launch(MainActivity::class.java)
    removeAllQuizzes(activityRule)
  }

  @After
  fun after() {
//    removeAllQuizzes()
  }

  @Test
  fun addQuizInFuture() {
    val c = Calendar.getInstance()
    c.add(Calendar.DATE, 1)
    addQuiz(c)
    checkMatches(c, 0)
  }

  @Test
  fun quizzesSortedAfterAdd() {
    val c = Calendar.getInstance()
    addQuiz(c)
    c.add(Calendar.DATE, 1)
    addQuiz(c)
    checkMatches(c, 0)
    c.add(Calendar.DATE, -1)
    checkMatches(c, 1)
  }

  @Test
  fun quizzesSortedAfterEdit() {
    val c = Calendar.getInstance()
    addQuiz(c)
    c.add(Calendar.DATE, 1)
    addQuiz(c)
    checkMatches(c, 0)
    c.add(Calendar.DATE, -1)
    checkMatches(c, 1)
    onView(withId(R.id.recyclerview_main)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        0, MyViewAction.clickChildViewWithId(R.id.ivEditDate)
      )
    )

    c.add(Calendar.MONTH, 1)
    // Edit quiz date.
    onView(
      withClassName(
        Matchers.equalTo(
          DatePicker::class.java.name
        )
      )
    ).perform(
      PickerActions.setDate(
        c[Calendar.YEAR],
        c[Calendar.MONTH] + 1,
        c[Calendar.DATE]
      )
    )
    onView(withId(android.R.id.button1)).perform(ViewActions.click())

    checkMatches(c, 0)
    c.add(Calendar.MONTH, -1)
    checkMatches(c, 1)
  }

  private fun addQuiz(year: Int, monthOfYear: Int, dayOfMonth: Int) {
    floatingActionButton.perform(ViewActions.click())
    onView(
      withClassName(
        Matchers.equalTo(
          DatePicker::class.java.name
        )
      )
    ).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth))
    onView(withId(android.R.id.button1)).perform(ViewActions.click())
  }

  private fun addQuiz(c: Calendar) =
    addQuiz(c[Calendar.YEAR], c[Calendar.MONTH] + 1, c[Calendar.DATE])

  private val fAB: ViewInteraction
    get() = onView(
      Matchers.allOf(
        withId(R.id.fab), childAtPosition(
          childAtPosition(withId(android.R.id.content), 0), 2
        ), isDisplayed()
      )
    )

  companion object {
    private fun childAtPosition(
      parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {
      return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
          description.appendText(
            "Child at position " + position + " in parent "
          )
          parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
          val parent = view.parent
          return (parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(
            position
          ))
        }
      }
    }

    fun removeAllQuizzes(activityRule: ActivityScenarioRule<MainActivity>) {
      var count = 0
      runBlocking {
        activityRule.scenario.onActivity {
          val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerview_main)
          count = recyclerView.adapter?.itemCount ?: 0
        }
      }
      try {
        repeat(count) {
          onView(withId(R.id.recyclerview_main)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
              0, ViewActions.swipeLeft()
            )
          )
        }
      } catch (_: Exception) {

      }
      val recyclerView = onView(withId(R.id.recyclerview_main))
//      recyclerView.check(RecyclerViewItemCountAssertion(0))
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

    fun checkMatches(c: Calendar, position: Int) {
      onView(withId(R.id.recyclerview_main)).check(
        matches(
          atPosition(
            position,
            hasDescendant(
              withText(
                containsString(
                  SimpleDateFormat("EEE, dd MMM yyyy").format(c.time)
                )
              )
            )
          )
        )
      )
    }

//    fun checkMatchString(string: String): (Int) -> Unit =
//      { position: Int -> checkMatches(position, string) }
  }
}