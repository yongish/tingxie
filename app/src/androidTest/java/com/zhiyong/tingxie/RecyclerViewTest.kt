package com.zhiyong.tingxie

import android.content.Context
import android.view.View
import androidx.test.filters.LargeTest
import org.junit.runner.RunWith
import com.zhiyong.tingxie.ui.main.MainActivity
import androidx.test.espresso.ViewInteraction
import org.junit.Before
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.PickerActions
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.*
import org.junit.Test
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class RecyclerViewTest {
  private val floatingActionButton = fAB

  @Before
  fun before() {
    ApplicationProvider.getApplicationContext<Context>()
      .deleteDatabase("pinyin_database")
    launch(MainActivity::class.java)
  }

  @Test
  fun addQuizInFuture() {
    val c = Calendar.getInstance()
    c.add(Calendar.DATE, 1)
    val year = c[Calendar.YEAR]
    val monthOfYear = c[Calendar.MONTH]
    val dayOfMonth = c[Calendar.DATE]
    addQuiz(year, monthOfYear, dayOfMonth)
    onView(ViewMatchers.withId(R.id.recyclerview_main))
      .check(
        ViewAssertions.matches(
          atPosition(
            0, ViewMatchers.hasDescendant(
              ViewMatchers.withText(
                CoreMatchers.containsString(dayOfMonth.toString())
              )
            )
          )
        )
      )
    onView(ViewMatchers.withId(R.id.recyclerview_main)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        0,
        ViewActions.swipeLeft()
      )
    )
  }

  @Test
  fun quizzesSortedAfterAdd() {
    addQuiz(2019, 3, 1)
    addQuiz(2019, 2, 28)
    //        onView(withId(R.id.recyclerview_main))
//                .check(matches(atPosition(0, hasDescendant(withText(containsString(
//                        "01 Mar 2019"))))));
//        onView(withId(R.id.recyclerview_main))
//                .check(matches(atPosition(1, hasDescendant(withText(containsString(
//                        "28 Feb 2019"))))));
//        removeQuiz(0);
//        removeQuiz(0);
  }

  @Test
  fun quizzesSortedAfterEdit() {
    addQuiz(2019, 3, 1)
    addQuiz(2019, 3, 2)
    onView(ViewMatchers.withId(R.id.recyclerview_main))
      .check(
        ViewAssertions.matches(
          atPosition(
            0, ViewMatchers.hasDescendant(
              ViewMatchers.withText(
                CoreMatchers.containsString(
                  "02 Mar 2019"
                )
              )
            )
          )
        )
      )
    onView(ViewMatchers.withId(R.id.recyclerview_main))
      .check(
        ViewAssertions.matches(
          atPosition(
            1, ViewMatchers.hasDescendant(
              ViewMatchers.withText(
                CoreMatchers.containsString(
                  "01 Mar 2019"
                )
              )
            )
          )
        )
      )
    onView(ViewMatchers.withId(R.id.recyclerview_main)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        0, MyViewAction.clickChildViewWithId(R.id.ivEditDate)
      )
    )
    onView(
      ViewMatchers.withClassName(
        Matchers.equalTo(
          DatePicker::class.java.name
        )
      )
    )
      .perform(PickerActions.setDate(2019, 4, 2))
    onView(ViewMatchers.withId(android.R.id.button1))
      .perform(ViewActions.click())
    onView(ViewMatchers.withId(R.id.recyclerview_main))
      .check(
        ViewAssertions.matches(
          atPosition(
            0, ViewMatchers.hasDescendant(
              ViewMatchers.withText(
                CoreMatchers.containsString(
                  "02 Apr 2019"
                )
              )
            )
          )
        )
      )
    onView(ViewMatchers.withId(R.id.recyclerview_main))
      .check(
        ViewAssertions.matches(
          atPosition(
            1, ViewMatchers.hasDescendant(
              ViewMatchers.withText(
                CoreMatchers.containsString(
                  "01 Mar 2019"
                )
              )
            )
          )
        )
      )
    removeQuiz(0)
    removeQuiz(0)
  }

  private fun addQuiz(year: Int, monthOfYear: Int, dayOfMonth: Int) {
    floatingActionButton.perform(ViewActions.click())
    onView(
      ViewMatchers.withClassName(
        Matchers.equalTo(
          DatePicker::class.java.name
        )
      )
    )
      .perform(PickerActions.setDate(year, monthOfYear, dayOfMonth))
    onView(ViewMatchers.withId(android.R.id.button1))
      .perform(ViewActions.click())
  }

  private fun removeQuiz(position: Int) {
    onView(ViewMatchers.withId(R.id.recyclerview_main)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        position,
        ViewActions.swipeLeft()
      )
    )
  }

  private val fAB: ViewInteraction
    get() = onView(
      Matchers.allOf(
        ViewMatchers.withId(R.id.fab),
        childAtPosition(
          childAtPosition(
            ViewMatchers.withId(android.R.id.content),
            0
          ),
          2
        ),
        ViewMatchers.isDisplayed()
      )
    )

  companion object {
    private fun childAtPosition(
      parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {
      return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
          description.appendText(
            "Child at position " +
                position + " in parent "
          )
          parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
          val parent = view.parent
          return (parent is ViewGroup
              && parentMatcher.matches(parent)
              && view == parent.getChildAt(position))
        }
      }
    }

    fun atPosition(position: Int, itemMatcher: Matcher<View?>): BoundedMatcher<View?, RecyclerView> {
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