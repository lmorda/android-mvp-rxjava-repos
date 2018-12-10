package com.example.lmorda.rxrepos.repos;

import android.support.test.filters.LargeTest;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.example.lmorda.rxrepos.R;
import com.example.lmorda.rxrepos.TestUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MockReposScreenTest {

    private final static String JAVA_REPO_NAME = "android-mvp-rxjava-repos";
    private final static String KOTLIN_REPO_NAME = "kotlin-mvi-rxjava-retrofit";

    @Rule
    public ActivityTestRule<ReposActivity> mReposActivityTestRule =
            new ActivityTestRule<>(ReposActivity.class);

    @Before
    public void setUp() throws Exception {
        IdlingRegistry.getInstance().register(
                mReposActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(
                mReposActivityTestRule.getActivity().getCountingIdlingResource());
    }


    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(ListView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA LV with text " + itemText);
            }
        };
    }

    @Test
    public void showAllRepos() {
        viewAllRepos();
        onView(withItemText(JAVA_REPO_NAME)).check(matches(isDisplayed()));
        onView(withItemText(KOTLIN_REPO_NAME)).check(matches(isDisplayed()));
    }

    @Test
    public void showJavaRepos() {
        viewJavaRepos();
        onView(withItemText(JAVA_REPO_NAME)).check(matches(isDisplayed()));
        onView(withItemText(KOTLIN_REPO_NAME)).check(doesNotExist());
    }

    @Test
    public void showKotlinRepos() {
        viewKotlinRepos();
        onView(withItemText(JAVA_REPO_NAME)).check(doesNotExist());
        onView(withItemText(KOTLIN_REPO_NAME)).check(matches(isDisplayed()));
    }

    @Test
    public void orientationChange_FilterJavaPersists() {
        viewJavaRepos();
        onView(withItemText(JAVA_REPO_NAME)).check(matches(isDisplayed()));
        onView(withItemText(KOTLIN_REPO_NAME)).check(doesNotExist());
        TestUtils.rotateOrientation(mReposActivityTestRule.getActivity());
        onView(withItemText(JAVA_REPO_NAME)).check(matches(isDisplayed()));
        onView(withItemText(KOTLIN_REPO_NAME)).check(doesNotExist());
    }

    @Test
    public void orientationChange_FilterKotlinPersists() {
        viewKotlinRepos();
        onView(withItemText(JAVA_REPO_NAME)).check(doesNotExist());
        onView(withItemText(KOTLIN_REPO_NAME)).check(matches(isDisplayed()));
        TestUtils.rotateOrientation(mReposActivityTestRule.getActivity());
        onView(withItemText(JAVA_REPO_NAME)).check(doesNotExist());
        onView(withItemText(KOTLIN_REPO_NAME)).check(matches(isDisplayed()));
    }

    private void viewAllRepos() {
        onView(withId(R.id.menu_filter)).perform(click());
        onView(withText("All")).perform(click());
    }

    private void viewJavaRepos() {
        onView(withId(R.id.menu_filter)).perform(click());
        onView(withText("Java")).perform(click());
    }

    private void viewKotlinRepos() {
        onView(withId(R.id.menu_filter)).perform(click());
        onView(withText("Kotlin")).perform(click());
    }
}