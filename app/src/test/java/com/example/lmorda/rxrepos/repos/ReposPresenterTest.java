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

import com.example.lmorda.rxrepos.data.Repo;
import com.example.lmorda.rxrepos.data.source.ReposRepository;
import com.example.lmorda.rxrepos.util.schedulers.BaseSchedulerProvider;
import com.example.lmorda.rxrepos.util.schedulers.ImmediateSchedulerProvider;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Flowable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReposPresenterTest {
    
    private static List<Repo> REPOS;

    @Mock
    private ReposRepository mReposRepository;

    @Mock
    private ReposContract.View mReposView;

    private BaseSchedulerProvider mSchedulerProvider;

    private ReposPresenter mReposPresenter;

    @Before
    public void setupReposPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Make the sure that all schedulers are immediate.
        mSchedulerProvider = new ImmediateSchedulerProvider();

        // Get a reference to the class under test
        mReposPresenter = new ReposPresenter(mReposRepository, mReposView, mSchedulerProvider);

        // The presenter won't update the view unless it's active.
        when(mReposView.isActive()).thenReturn(true);

        // We subscribe the repos to 3, with one java and two kotlin
        REPOS = Lists.newArrayList(new Repo(1, "Name", "Description", "Url", "Java", "2018-12-13T08:34:04Z"),
                new Repo(2, "Name2", "Description2", "Url2", "Java", "2018-12-13T08:34:04Z"),
                new Repo(3, "Name3", "Description3", "Url3", "Kotlin", "2018-12-13T08:34:04Z"));
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
        mReposPresenter = new ReposPresenter(mReposRepository, mReposView, mSchedulerProvider);

        // Then the presenter is set to the view
        verify(mReposView).setPresenter(mReposPresenter);
    }

    @Test
    public void loadAllReposFromRepositoryAndLoadIntoView() {
        // Given an initialized ReposPresenter with initialized repos
        when(mReposRepository.getRepos()).thenReturn(Flowable.just(REPOS));
        // When loading of Repos is requested
        mReposPresenter.setFiltering(ReposFilterType.ALL_REPOS);
        mReposPresenter.loadRepos(true);

        // Then progress indicator is shown
        verify(mReposView).setLoadingIndicator(true);
        // Then progress indicator is hidden and all repos are shown in UI
        verify(mReposView).setLoadingIndicator(false);
    }

    @Test
    public void loadJavaReposFromRepositoryAndLoadIntoView() {
        // Given an initialized ReposPresenter with initialized repos
        when(mReposRepository.getRepos()).thenReturn(Flowable.just(REPOS));
        // When loading of Repos is requested
        mReposPresenter.setFiltering(ReposFilterType.JAVA_REPOS);
        mReposPresenter.loadRepos(true);

        // Then progress indicator is hidden and active repos are shown in UI
        verify(mReposView).setLoadingIndicator(false);
    }

    @Test
    public void loadKotlinReposFromRepositoryAndLoadIntoView() {
        // Given an initialized ReposPresenter with initialized repos
        when(mReposRepository.getRepos()).thenReturn(Flowable.just(REPOS));
        // When loading of Repos is requested
        mReposPresenter.setFiltering(ReposFilterType.KOTLIN_REPOS);
        mReposPresenter.loadRepos(true);

        // Then progress indicator is hidden and completed repos are shown in UI
        verify(mReposView).setLoadingIndicator(false);
    }

    @Test
    public void errorLoadingRepos_ShowsError() {
        // Given that no repos are available in the repository
        when(mReposRepository.getRepos()).thenReturn(Flowable.error(new Exception()));

        // When repos are loaded
        mReposPresenter.setFiltering(ReposFilterType.ALL_REPOS);
        mReposPresenter.loadRepos(true);

        // Then an error message is shown
        verify(mReposView).showLoadingReposError("Could not get repositories!");
    }

}
