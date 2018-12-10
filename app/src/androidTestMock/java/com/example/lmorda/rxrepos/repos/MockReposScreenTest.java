/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lmorda.rxrepos.repos;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;

import com.example.lmorda.rxrepos.Injection;
import com.example.lmorda.rxrepos.data.Repo;
import com.example.lmorda.rxrepos.data.source.ReposRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MockReposScreenTest {

    @Rule
    public ActivityTestRule<ReposActivity> mReposActivityTestRule =
            new ActivityTestRule<>(ReposActivity.class, true, false);

    @Before
    public void intentWithStubbedRepoId() {
        // Given some repos
        ReposRepository.destroyInstance();
        ReposRepository repository = Injection.provideReposRepository(InstrumentationRegistry.getContext());
        repository.saveRepo(new Repo(1, "Name1", "", "", "Java"));
        repository.saveRepo(new Repo(2, "Name2", "", "", "Kotlin"));

        // Lazily start the Activity from the ActivityTestRule
        Intent startIntent = new Intent();
        mReposActivityTestRule.launchActivity(startIntent);
    }

    @Test
    public void repos_ShowsNonEmptyMessage() throws Exception {
        // Check that the active and completed repos text is displayed
        String expectedActiveTaskText = InstrumentationRegistry.getTargetContext()
                .getString(R.string.statistics_active_repos);
        onView(withText(containsString(expectedActiveTaskText))).check(matches(isDisplayed()));
        String expectedCompletedTaskText = InstrumentationRegistry.getTargetContext()
                .getString(R.string.statistics_completed_repos);
        onView(withText(containsString(expectedCompletedTaskText))).check(matches(isDisplayed()));
    }
}
