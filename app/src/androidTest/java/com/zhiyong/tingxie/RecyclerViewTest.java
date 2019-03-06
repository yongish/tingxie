package com.zhiyong.tingxie;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

import com.zhiyong.tingxie.ui.main.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecyclerViewTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private ViewInteraction floatingActionButton = getFAB();

    @Before
    public void before() {
        InstrumentationRegistry.getTargetContext().deleteDatabase("pinyin_database");
    }

    @Test
    public void addQuizInFuture() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        int year = c.get(Calendar.YEAR);
        int monthOfYear = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DATE);
        addQuiz(year, monthOfYear, dayOfMonth);

        onView(withId(R.id.recyclerview_main))
                .check(matches(atPosition(0, hasDescendant(withText(containsString(
                        String.valueOf(dayOfMonth)
                ))))));

        onView(withId(R.id.recyclerview_main)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
    }

    @Test
    public void quizzesSortedAfterAdd() {
        addQuiz(2019, 3, 1);
        addQuiz(2019, 2, 28);
        onView(withId(R.id.recyclerview_main))
                .check(matches(atPosition(0, hasDescendant(withText(containsString(
                        "28 Feb 2019"))))));
        onView(withId(R.id.recyclerview_main))
                .check(matches(atPosition(1, hasDescendant(withText(containsString(
                        "01 Mar 2019"))))));
        removeQuiz(0);
        removeQuiz(0);
    }

    @Test
    public void quizzesSortedAfterEdit() {
        addQuiz(2019, 3, 1);
        addQuiz(2019, 3, 2);

        onView(withId(R.id.recyclerview_main))
                .check(matches(atPosition(0, hasDescendant(withText(containsString(
                        "01 Mar 2019"))))));
        onView(withId(R.id.recyclerview_main))
                .check(matches(atPosition(1, hasDescendant(withText(containsString(
                        "02 Mar 2019"))))));
        onView(withId(R.id.recyclerview_main)).perform(
                RecyclerViewActions.actionOnItemAtPosition(
                        0, MyViewAction.clickChildViewWithId(R.id.ivEditDate)
                )
        );

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2019, 4, 2));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.recyclerview_main))
                .check(matches(atPosition(0, hasDescendant(withText(containsString(
                        "02 Mar 2019"))))));
        onView(withId(R.id.recyclerview_main))
                .check(matches(atPosition(1, hasDescendant(withText(containsString(
                        "02 Apr 2019"))))));

        removeQuiz(0);
        removeQuiz(0);
    }

    private void addQuiz(int year, int monthOfYear, int dayOfMonth) {
        floatingActionButton.perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }

    private void removeQuiz(int position) {
        onView(withId(R.id.recyclerview_main)).perform(
                RecyclerViewActions.actionOnItemAtPosition(position, swipeLeft()));
    }

    private ViewInteraction getFAB() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        return floatingActionButton;
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " +
                        position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup
                        && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

//        ViewInteraction textView = onView(
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
