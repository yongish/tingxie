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
    removeAllQuizzes()
  }

  @After
  fun after() {
//    removeAllQuizzes()
  }

  @Test
  fun addQuizInFuture() {
    val c = Calendar.getInstance()
    c.add(Calendar.DATE, 1)
    val dayOfMonth = c[Calendar.DATE]
    addQuiz(c)
    onView(withId(R.id.recyclerview_main)).check(
      matches(
        atPosition(
          0, hasDescendant(
            withText(
              containsString(dayOfMonth.toString())
            )
          )
        )
      )
    )
    onView(withId(R.id.recyclerview_main)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        0, ViewActions.swipeLeft()
      )
    )
  }

  @Test
  fun myTest() {
    val c = Calendar.getInstance()
    addQuiz(c)

    var count = 0
    activityRule.scenario.onActivity {
      val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerview_main)
      count = recyclerView.adapter?.itemCount ?: 0
    }
    repeat(count) {
      removeQuiz(0)
    }
  }

//  @Test
//  fun mainActivityTest() {
//    while (true) {
//      onView(withId(R.id.recyclerview_main)).perform(
//        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//          0, ViewActions.swipeLeft()
//        )
//      )
//    }
//  }

  @Test
  fun quizzesSortedAfterAdd() {
    val c = Calendar.getInstance()
    addQuiz(c)
    c.add(Calendar.DATE, 1)
    addQuiz(c)
    val format = SimpleDateFormat("EEE, dd MMM yyyy")
    onView(withId(R.id.recyclerview_main)).check(
      matches(
        atPosition(
          0,
          hasDescendant(
            withText(
              containsString(
                format.format(c.time)
              )
            )
          )
        )
      )
    )
    c.add(Calendar.DATE, -1)
    onView(withId(R.id.recyclerview_main)).check(
      matches(
        atPosition(
          1,
          hasDescendant(
            withText(
              containsString(
                format.format(c.time)
              )
            )
          )
        )
      )
    )

  }

  @Test
  fun quizzesSortedAfterEdit() {
    val c = Calendar.getInstance()
    addQuiz(c)
//    addQuiz(2019, 3, 1)
    c.add(Calendar.DATE, 1)
    addQuiz(c)
//    addQuiz(2019, 3, 2)
    val format = SimpleDateFormat("EEE, dd MMM yyyy")
    onView(withId(R.id.recyclerview_main)).check(
      matches(
        atPosition(
          0, hasDescendant(
            withText(
              containsString(
                format.format(c.time)
//                "02 Mar 2019"
              )
            )
          )
        )
      )
    )
    c.add(Calendar.DATE, -1)
    onView(withId(R.id.recyclerview_main)).check(
      matches(
        atPosition(
          1, hasDescendant(
            withText(
              containsString(
                format.format(c.time)
//                "01 Mar 2019"
              )
            )
          )
        )
      )
    )

    onView(withId(R.id.recyclerview_main)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        0, MyViewAction.clickChildViewWithId(R.id.ivEditDate)
      )
    )

    c.add(Calendar.MONTH, 1)
    onView(
      withClassName(
        Matchers.equalTo(
          DatePicker::class.java.name
        )
      )
    ).perform(PickerActions.setDate(c[Calendar.YEAR], c[Calendar.MONTH] + 1, c[Calendar.DATE]))

    onView(withId(android.R.id.button1)).perform(ViewActions.click())
    onView(withId(R.id.recyclerview_main)).check(
      matches(
        atPosition(
          0, hasDescendant(
            withText(
              containsString(
                format.format(c.time)
//                "02 Apr 2019"
              )
            )
          )
        )
      )
    )
    c.add(Calendar.MONTH, -1)
    onView(withId(R.id.recyclerview_main)).check(
      matches(
        atPosition(
          1, hasDescendant(
            withText(
              containsString(
                format.format(c.time)
//                "01 Mar 2019"
              )
            )
          )
        )
      )
    )
  }

//  private fun addQuiz(year: Int, monthOfYear: Int, dayOfMonth: Int) {
//    floatingActionButton.perform(ViewActions.click())
//    onView(
//      withClassName(
//        Matchers.equalTo(
//          DatePicker::class.java.name
//        )
//      )
//    ).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth))
//    onView(withId(android.R.id.button1)).perform(ViewActions.click())
//  }

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

  private fun addQuiz(c: Calendar) = addQuiz(c[Calendar.YEAR], c[Calendar.MONTH] + 1, c[Calendar.DATE])

  private fun removeQuiz(position: Int) {
    onView(withId(R.id.recyclerview_main)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        position, ViewActions.swipeLeft()
      )
    )
//    onView(withId(R.id.recyclerview_main)).perform(
//      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//        0, ViewActions.swipeLeft()
//      )
//    )
  }

  private fun removeAllQuizzes() {
    var count = 0
    activityRule.scenario.onActivity {
      val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerview_main)
      count = recyclerView.adapter?.itemCount ?: 0
    }
    repeat(count) {
      removeQuiz(0)
    }
    val recyclerView = onView(withId(R.id.recyclerview_main))
    recyclerView.check(RecyclerViewItemCountAssertion(0))
  }

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

    fun atPosition(
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
    } //        ViewInteraction textView = onView(
    //                allOf(withId(R.id.word), withText("+ Word 20"),
    //                        childAtPosition(
    //                                childAtPosition(
    //                                        withId(R.id.recyclerview_main),
    //                                        13),
    //                                0),
    //                        isDisplayed()));
    //        textView.check(matches(withText("+ Word 20")));
    //    MainActivity activity = mActivityTestRule.getActivity();
    //    RecyclerView recyclerView = activity.findViewById(R.id.recyclerview_main);
  }
}