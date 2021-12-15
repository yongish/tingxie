package com.zhiyong.tingxie

import android.content.Context
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.zhiyong.tingxie.ui.main.MainActivity
import org.hamcrest.*
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

  @Before
  fun init() {
    getApplicationContext<Context>().deleteDatabase("pinyin_database")
    ActivityScenario.launch(MainActivity::class.java)
  }

  @Test
  fun twoQuizzes() {
    // Test add and remove quizzes and words.
    addQuiz(2021, 12, 1)
    addQuiz(2021, 12, 2)
    tapAddViewWordButton(0)
    addWord("脚踏实地")
    addWord("卷心菜")
    tapBackButton()
    onView(withId(R.id.recyclerview_main)).check(matches(atPosition(
      0, hasDescendant(withText("2/2 remaining on round 1"))
    )))
    tapAddViewWordButton(1)
    addWord("杏")
    addWord("疫苗")
    tapBackButton()
    removeQuiz(1)
    onView(withText("Undo")).perform(click())
    tapAddViewWordButton(1)
    onView(withId(R.id.recyclerview_word)).check(RecyclerViewItemCountAssertion(2))
    checkWord(0, "杏")
    checkWord(1, "疫苗")
    tapBackButton()
    tapAddViewWordButton(0)
    onView(withId(R.id.recyclerview_word)).check(RecyclerViewItemCountAssertion(2))
    checkWord(0, "脚踏实地")
    checkWord(1, "卷心菜")
    tapBackButton()
    // Test quiz functionality.
    onView(withId(R.id.recyclerview_main)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        0, clickOnViewChild(R.id.btnStartResume)
      )
    )
    onView(withId(R.id.btnShowAnswer)).perform(click())
    onView(withId(R.id.btnAnswerCorrect)).perform(click())
    tapBackButton()
    onView(withId(R.id.recyclerview_main)).check(matches(atPosition(
      0, hasDescendant(withText("1/2 remaining on round 1"))
    )))
  }

  private fun addQuiz(year: Int?, monthOfYear: Int?, dayOfMonth: Int?) {
    onView(withId(R.id.fab)).perform(click())
    if (year != null && monthOfYear != null && dayOfMonth != null) {
      onView(withClassName(equalTo(DatePicker::class.java.name)))
        .perform(PickerActions.setDate(year, monthOfYear, dayOfMonth))
    }
    onView(withId(android.R.id.button1)).perform(click())
  }

  private fun removeQuiz(position: Int) {
    onView(withId(R.id.recyclerview_main)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        position,
        swipeLeft()
      )
    )
  }

  private fun tapAddViewWordButton(position: Int) {
    onView(withId(R.id.recyclerview_main)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        position, clickOnViewChild(R.id.btnAddViewWords)
      )
    )
  }

  private fun addWord(word: String) {
    onView(withId(R.id.fab)).perform(click())
    onView(withId(R.id.autoCompleteTextView1)).perform(replaceText(word))
    onView(withText("OK")).perform(click())
  }

  private fun tapBackButton() {
    onView(allOf(instanceOf(AppCompatImageButton::class.java), withParent(withId(R.id.toolbar)))).perform(click())
  }

  private fun checkWord(position: Int, word: String) {
    onView(withId(R.id.recyclerview_word)).check(matches(atPosition(
      position, hasDescendant(withText(word))
    )))
  }

  private fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getConstraints() = null
    override fun getDescription() = "Click on a child view with specified id."
    override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
  }

  private fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View?>? {
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
}
